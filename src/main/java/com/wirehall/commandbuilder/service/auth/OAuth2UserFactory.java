package com.wirehall.commandbuilder.service.auth;

import com.wirehall.commandbuilder.dto.User;
import com.wirehall.commandbuilder.exception.OAuth2AuthProcessingException;
import com.wirehall.commandbuilder.model.props.USER_PROPERTY;

import java.util.Map;

public class OAuth2UserFactory {

    public static User getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        User user = new User();
        user.addProperty(USER_PROPERTY.providerId, registrationId);
        user.addProperty(USER_PROPERTY.name, attributes.get(USER_PROPERTY.name.toString()));
        user.addProperty(USER_PROPERTY.email, attributes.get(USER_PROPERTY.email.toString()));

        if (registrationId.equalsIgnoreCase(User.OAUTH_PROVIDER.google.toString())) {
            user.setId(attributes.get("sub"));
            user.addProperty(USER_PROPERTY.provider, User.OAUTH_PROVIDER.google.toString());
            user.addProperty(USER_PROPERTY.imageUrl, attributes.get("picture"));
            return user;
        } else if (registrationId.equalsIgnoreCase(User.OAUTH_PROVIDER.facebook.toString())) {
            user.setId(attributes.get("id"));
            user.addProperty(USER_PROPERTY.provider, User.OAUTH_PROVIDER.facebook.toString());
            user.addProperty(USER_PROPERTY.imageUrl, attributes.get(getFacebookImageUrl(attributes)));
            return user;
        } else if (registrationId.equalsIgnoreCase(User.OAUTH_PROVIDER.github.toString())) {
            user.setId(attributes.get("id"));
            user.addProperty(USER_PROPERTY.provider, User.OAUTH_PROVIDER.github.toString());
            user.addProperty(USER_PROPERTY.imageUrl, attributes.get("avatar_url"));
            return user;
        } else {
            throw new OAuth2AuthProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
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
