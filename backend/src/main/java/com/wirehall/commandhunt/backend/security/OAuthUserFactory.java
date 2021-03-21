package com.wirehall.commandhunt.backend.security;

import com.wirehall.commandhunt.backend.dto.User;
import com.wirehall.commandhunt.backend.dto.User.OAuthProvider;
import com.wirehall.commandhunt.backend.exception.OAuthException;

import java.util.Map;

public class OAuthUserFactory {

  private OAuthUserFactory() {
    // Utility classes should not have public constructors.
  }

  /**
   * Factory used to get the user dto with the info received from OAuth provider.
   *
   * @param registrationId The user's registration id received from provider.
   * @param attributes     User attributes received from provider.
   * @return User DTO.
   */
  public static User getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
      User user = new User();
      user.setProvider(OAuthProvider.valueOf(registrationId));
      user.setName((String) attributes.get("name"));
      user.setEmail((String) attributes.get("email"));

      if (registrationId.equalsIgnoreCase(OAuthProvider.google.toString())) {
          user.setProviderId((String) attributes.get("sub"));
          user.setImageUrl((String) attributes.get("picture"));
          user.setEmailVerified((boolean) attributes.get("email_verified"));
          return user;
      } else if (registrationId.equalsIgnoreCase(OAuthProvider.facebook.toString())) {
          user.setProviderId((String) attributes.get("id"));
          user.setImageUrl(getFacebookImageUrl(attributes));
          return user;
      } else if (registrationId.equalsIgnoreCase(OAuthProvider.github.toString())) {
          user.setProviderId((String) attributes.get("id"));
          user.setImageUrl((String) attributes.get("avatar_url"));
          return user;
      } else {
          throw new OAuthException("Sorry! Login with " + registrationId + " is not supported yet.");
      }
  }

  private static String getFacebookImageUrl(Map<String, Object> attributes) {
    if (attributes.containsKey("picture")) {
      Map<String, Object> pictureObj = (Map<String, Object>) attributes.get("picture");
      if (pictureObj.containsKey("data")) {
        Map<String, Object> dataObj = (Map<String, Object>) pictureObj.get("data");
        if (dataObj.containsKey("url")) {
          return (String) dataObj.get("url");
        }
      }
    }
    return null;
  }
}
