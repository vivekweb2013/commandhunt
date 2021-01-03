package com.wirehall.commandhunt.backend.security;

import com.wirehall.commandhunt.backend.dto.User;
import com.wirehall.commandhunt.backend.dto.User.OAuthProvider;
import com.wirehall.commandhunt.backend.exception.OAuthException;
import com.wirehall.commandhunt.backend.model.props.UserProperty;
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
    user.addProperty(UserProperty.PROVIDER, registrationId);
    user.addProperty(UserProperty.NAME, attributes.get(UserProperty.NAME.toLowerCase()));
    user.addProperty(UserProperty.EMAIL, attributes.get(UserProperty.EMAIL.toLowerCase()));

    if (registrationId.equalsIgnoreCase(OAuthProvider.google.toString())) {
      user.addProperty(UserProperty.PROVIDER_ID, attributes.get("sub"));
      user.addProperty(UserProperty.IMAGE_URL, attributes.get("picture"));
      user.addProperty(UserProperty.EMAIL_VERIFIED, attributes.get("email_verified"));
      return user;
    } else if (registrationId.equalsIgnoreCase(OAuthProvider.facebook.toString())) {
      user.addProperty(UserProperty.PROVIDER_ID, attributes.get("id"));
      user.addProperty(UserProperty.IMAGE_URL, getFacebookImageUrl(attributes));
      return user;
    } else if (registrationId.equalsIgnoreCase(OAuthProvider.github.toString())) {
      user.addProperty(UserProperty.PROVIDER_ID, attributes.get("id"));
      user.addProperty(UserProperty.IMAGE_URL, attributes.get("avatar_url"));
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
