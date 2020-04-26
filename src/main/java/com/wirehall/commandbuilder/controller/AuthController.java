package com.wirehall.commandbuilder.controller;

import com.wirehall.commandbuilder.dto.User;
import com.wirehall.commandbuilder.dto.auth.Login;
import com.wirehall.commandbuilder.dto.auth.SignUp;
import com.wirehall.commandbuilder.model.auth.CustomUserPrincipal;
import com.wirehall.commandbuilder.security.CurrentUser;
import com.wirehall.commandbuilder.service.UserService;
import com.wirehall.commandbuilder.util.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserService userService;

  @Autowired
  private JWTUtil jwtUtil;

  /**
   * @param loginRequest Login request from the client
   * @return Response indicating the authentication status
   */
  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody Login loginRequest) {
    LOGGER.info(loginRequest.toString());

    Authentication authentication =
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    User user = ((CustomUserPrincipal) authentication.getPrincipal()).getUser();
    String token = jwtUtil.createToken(authentication, 864000000L);

    Map<String, Object> resp =
            new HashMap<String, Object>() {
              {
                put("user", user);
                put("token", token);
              }
            };

    return ResponseEntity.ok(resp);
  }

  /**
   * @param signUpRequest Sign up request from client
   * @return Response indicating the status of sign up action
   */
  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignUp signUpRequest) {
    LOGGER.info(signUpRequest.toString());
    User user = userService.addUser(signUpRequest);

    URI location =
            ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/user/me")
                    .buildAndExpand(user.getId())
                    .toUri();

    return ResponseEntity.created(location).body(user);
  }

  /**
   * @param customUserPrincipal User principal
   * @return Currently logged in user
   */
  @GetMapping("/user/me")
  @PreAuthorize("hasRole('USER')")
  public User getCurrentUser(@CurrentUser CustomUserPrincipal customUserPrincipal) {
    return customUserPrincipal.getUser();
  }
}
