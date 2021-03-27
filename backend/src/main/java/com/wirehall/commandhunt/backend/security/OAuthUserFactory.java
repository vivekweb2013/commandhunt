package com.wirehall.commandhunt.backend.security;

import com.wirehall.commandhunt.backend.exception.OAuthException;
import com.wirehall.commandhunt.backend.model.UserEntity;
import com.wirehall.commandhunt.backend.model.UserEntity.OAuthProvider;
import java.util.Map;

public class OAuthUserFactory {

  private OAuthUserFactory() {
    // Utility classes should not have public constructors.
  }

  /**
   * Factory used to get the user entity with the info received from OAuth provider.
   *
   * @param registrationId The user's registration id received from provider.
   * @param attributes     User attributes received from provider.
   * @return User entity.
   */
  public static UserEntity getOAuth2UserInfo(String registrationId,
      Map<String, Object> attributes) {
    UserEntity userEntity = new UserEntity();
    userEntity.setProvider(OAuthProvider.valueOf(registrationId.toUpperCase()));
    userEntity.setName((String) attributes.get("name"));
    userEntity.setEmail((String) attributes.get("email"));

    if (registrationId.equalsIgnoreCase(OAuthProvider.GOOGLE.toString())) {
      userEntity.setProviderId((String) attributes.get("sub"));
      userEntity.setImageUrl((String) attributes.get("picture"));
      userEntity.setEmailVerified((boolean) attributes.get("email_verified"));
      return userEntity;
    } else if (registrationId.equalsIgnoreCase(OAuthProvider.FACEBOOK.toString())) {
      userEntity.setProviderId((String) attributes.get("id"));
      userEntity.setImageUrl(getFacebookImageUrl(attributes));
      return userEntity;
    } else if (registrationId.equalsIgnoreCase(OAuthProvider.GITHUB.toString())) {
      userEntity.setProviderId((String) attributes.get("id"));
      userEntity.setImageUrl((String) attributes.get("avatar_url"));
      return userEntity;
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
