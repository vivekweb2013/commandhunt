package com.wirehall.commandhunt.backend.controller;

import com.wirehall.commandhunt.backend.dto.User;
import com.wirehall.commandhunt.backend.dto.auth.Login;
import com.wirehall.commandhunt.backend.dto.auth.SignUp;
import com.wirehall.commandhunt.backend.exception.BadRequestException;
import com.wirehall.commandhunt.backend.mapper.UserMapper;
import com.wirehall.commandhunt.backend.model.UserEntity;
import com.wirehall.commandhunt.backend.model.auth.CustomUserPrincipal;
import com.wirehall.commandhunt.backend.security.CurrentUser;
import com.wirehall.commandhunt.backend.service.UserService;
import com.wirehall.commandhunt.backend.util.AuthUtil;
import com.wirehall.commandhunt.backend.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
  private final UserMapper userMapper = new UserMapper();

  @Value("${app.oauth2.authorizedRedirectUris}")
  private String[] authorizedRedirectUris;

  @Value("${app.isManualAuthAllowed}")
  private boolean isManualAuthAllowed;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserService userService;

  @Autowired
  private JwtUtil jwtUtil;

  /**
   * The endpoint used by clients to login into the application with user credentials.
   * This is driven by the isManualAuthAllowed flag. If set false this manual login won't work.
   * This is intentionally kept as form-login type instead of json post since it needs to be
   * aligned with the OAuth style response mechanism of sending token/error using redirect.
   *
   * @param loginRequest Login request from the client.
   * @param redirectUri  The redirect url where token/error are processed on gui.
   * @return Response indicating the authentication status.
   */
  @PostMapping("/login")
  public ModelAndView authenticateUser(@Valid @ModelAttribute Login loginRequest,
                                       @RequestParam("redirect_uri") String redirectUri) {
    LOGGER.debug("Login requested: {}", loginRequest);

    if (!isManualAuthAllowed) {
      throw new BadRequestException("Manual login is not allowed, use OAuth login instead");
    }

    LOGGER.debug("Send redirect to: {}", redirectUri);
    String redirectUrl = "redirect:" + redirectUri + (redirectUri.contains("?") ? "&" : "?");

    try {
      if (!AuthUtil.isAuthorizedRedirectUri(redirectUri, authorizedRedirectUris)) {
        throw new BadRequestException("Invalid redirect_uri provided: " + redirectUri);
      }
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                      loginRequest.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);
      String token = jwtUtil.createToken(authentication, 864000000L);
      redirectUrl += "token=" + token;
      return new ModelAndView(redirectUrl);
    } catch (Exception ex) {
      redirectUrl += "error=Login Failed! Check your credentials.";
      return new ModelAndView(redirectUrl);
    }
  }

  /**
   * The endpoint used by clients to sign up with user credentials.
   *
   * @param signUpRequest Sign up request from client.
   * @return Response indicating the status of sign up action.
   */
  @PostMapping("/signup")
  public ResponseEntity<User> registerUser(@Valid @RequestBody SignUp signUpRequest) {
    LOGGER.debug("Signup requested: {}", signUpRequest);

    if (!isManualAuthAllowed) {
      throw new BadRequestException("Manual sign-up is not allowed at the moment");
    }

    User user = userService.registerUser(signUpRequest);
    URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/me")
            .buildAndExpand().toUri();

    return ResponseEntity.created(location).body(user);
  }

  /**
   * The endpoint used by clients to get the currently logged in user details.
   *
   * @param customUserPrincipal User principal.
   * @return Currently logged in user.
   */
  @GetMapping("/user/me")
  @PreAuthorize("hasRole('USER')")
  public User getCurrentUser(@CurrentUser CustomUserPrincipal customUserPrincipal) {
    UserEntity userEntity = customUserPrincipal.getUser();
    return userMapper.mapToUser(userEntity);
  }

  /**
   * Allows clients to enquire if manual signup and login is allowed.
   * This behaviour is driven by app.isManualAuthAllowed property.
   * If the property is set false then only OAuth login should be allowed and
   * both manual sign-up and login should be disabled by client.
   *
   * @return True or false depending on the value of app.isManualAuthAllowed property
   */
  @GetMapping("/isManualAuthAllowed")
  public Boolean isManualAuthAllowed() {
    return isManualAuthAllowed;
  }
}
