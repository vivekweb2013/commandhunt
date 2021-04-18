package com.wirehall.commandhunt.backend.util;

import java.net.URI;
import java.util.Arrays;

public class AuthUtil {
  private AuthUtil() {}

  public static boolean isAuthorizedRedirectUri(String uri, String[] authorizedRedirectUris) {
    URI clientRedirectUri = URI.create(uri);

    return Arrays.stream(authorizedRedirectUris)
        .anyMatch(
            authorizedRedirectUri -> {
              // Only validate host and port. Let the clients use different paths if they want to
              URI authorizedUri = URI.create(authorizedRedirectUri);
              return authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                  && authorizedUri.getPort() == clientRedirectUri.getPort();
            });
  }
}
