package com.wirehall.commandhunt.backend.security;

import com.wirehall.commandhunt.backend.exception.BadRequestException;
import com.wirehall.commandhunt.backend.util.CookieUtil;
import com.wirehall.commandhunt.backend.util.JwtUtil;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(OAuthSuccessHandler.class);

  private final CustomOAuthRequestRepository customOAuthRequestRepository;

  @Value("${app.oauth2.authorizedRedirectUris}")
  private String[] authorizedRedirectUris;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  OAuthSuccessHandler(CustomOAuthRequestRepository customOAuthRequestRepository) {
    this.customOAuthRequestRepository = customOAuthRequestRepository;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    String targetUrl = determineTargetUrl(request, response, authentication);

    if (response.isCommitted()) {
      LOGGER.warn("Response has already been committed. Unable to redirect to url: {}", targetUrl);
      return;
    }

    clearAuthenticationAttributes(request, response);
    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }

  @Override
  protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    Optional<String> redirectUri = CookieUtil.getCookie(request, CustomOAuthRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
        .map(Cookie::getValue);

    if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
      throw new BadRequestException(
          "Unauthorized Redirect URI, Can't proceed with the authentication");
    }

    String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

    String token = jwtUtil.createToken(authentication, 864000000L);

    return UriComponentsBuilder.fromUriString(targetUrl)
        .queryParam("token", token)
        .build().toUriString();
  }

  protected void clearAuthenticationAttributes(HttpServletRequest request,
      HttpServletResponse response) {
    super.clearAuthenticationAttributes(request);
    customOAuthRequestRepository.removeAuthorizationRequestCookies(request, response);
  }

  private boolean isAuthorizedRedirectUri(String uri) {
    URI clientRedirectUri = URI.create(uri);

    return Arrays.stream(authorizedRedirectUris).anyMatch(authorizedRedirectUri -> {
      // Only validate host and port. Let the clients use different paths if they want to
      URI authorizedUri = URI.create(authorizedRedirectUri);
      return authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
          && authorizedUri.getPort() == clientRedirectUri.getPort();
    });
  }
}
