package com.wirehall.commandbuilder.service.auth;

import com.wirehall.commandbuilder.dto.User;
import com.wirehall.commandbuilder.dto.User.OAuthProvider;
import com.wirehall.commandbuilder.exception.OAuth2Exception;
import com.wirehall.commandbuilder.model.props.UserProperty;
import java.util.Map;

public class OAuth2UserFactory {

  /**
   * Factory used to get the user dto with the info received from OAuth provider.
   *
   * @param registrationId The user's registration id received from provider.
   * @param attributes     User attributes received from provider.
   * @return User DTO.
   */
  public static User getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
    User user = new User();
    user.addProperty(UserProperty.providerId, registrationId);
    user.addProperty(UserProperty.name, attributes.get(UserProperty.name.toString()));
    user.addProperty(UserProperty.email, attributes.get(UserProperty.email.toString()));

    if (registrationId.equalsIgnoreCase(OAuthProvider.google.toString())) {
      user.setId(attributes.get("sub"));
      user.addProperty(UserProperty.provider, OAuthProvider.google.toString());
      user.addProperty(UserProperty.imageUrl, attributes.get("picture"));
      return user;
    } else if (registrationId.equalsIgnoreCase(OAuthProvider.facebook.toString())) {
      user.setId(attributes.get("id"));
      user.addProperty(UserProperty.provider, OAuthProvider.facebook.toString());
      user.addProperty(UserProperty.imageUrl, attributes.get(getFacebookImageUrl(attributes)));
      return user;
    } else if (registrationId.equalsIgnoreCase(OAuthProvider.github.toString())) {
      user.setId(attributes.get("id"));
      user.addProperty(UserProperty.provider, OAuthProvider.github.toString());
      user.addProperty(UserProperty.imageUrl, attributes.get("avatar_url"));
      return user;
    } else {
      throw new OAuth2Exception(
          "Sorry! Login with " + registrationId + " is not supported yet.");
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
