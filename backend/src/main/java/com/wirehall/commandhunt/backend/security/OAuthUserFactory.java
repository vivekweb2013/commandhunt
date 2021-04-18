package com.wirehall.commandhunt.backend.security;

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
   * @param oAuthProvider The user's oAuth provider.
   * @param attributes User attributes received from provider.
   * @return User entity.
   */
  public static UserEntity getOAuth2UserInfo(
      OAuthProvider oAuthProvider, Map<String, Object> attributes) {
    UserEntity userEntity = new UserEntity();
    userEntity.setProvider(oAuthProvider);
    userEntity.setName((String) attributes.get("name"));
    userEntity.setEmail((String) attributes.get("email"));

    if (oAuthProvider.equals(OAuthProvider.GOOGLE)) {
      userEntity.setProviderId(String.valueOf(attributes.get("sub")));
      userEntity.setImageUrl((String) attributes.get("picture"));
      userEntity.setEmailVerified((boolean) attributes.get("email_verified"));
    } else if (oAuthProvider.equals(OAuthProvider.FACEBOOK)) {
      userEntity.setProviderId(String.valueOf(attributes.get("id")));
      userEntity.setImageUrl(getFacebookImageUrl(attributes));
    } else if (oAuthProvider.equals(OAuthProvider.GITHUB)) {
      userEntity.setProviderId(String.valueOf(attributes.get("id")));
      userEntity.setImageUrl((String) attributes.get("avatar_url"));
    }
    return userEntity;
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
