package com.wirehall.commandhunt.backend.service.auth;


import com.wirehall.commandhunt.backend.dto.User;
import com.wirehall.commandhunt.backend.exception.OAuthException;
import com.wirehall.commandhunt.backend.model.auth.CustomUserPrincipal;
import com.wirehall.commandhunt.backend.repository.UserRepository;
import com.wirehall.commandhunt.backend.security.OAuthUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CustomOAuthUserService extends DefaultOAuth2UserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oauthUserRequest) {
    OAuth2User oauthUser = super.loadUser(oauthUserRequest);

    try {
      return processOAuth2User(oauthUserRequest, oauthUser);
    } catch (AuthenticationException ex) {
      throw ex;
    } catch (Exception ex) {
      // Throwing an instance of AuthenticationException,
      // This will trigger the OAuthFailureHandler
      throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
    }
  }

  private OAuth2User processOAuth2User(OAuth2UserRequest oauth2UserRequest, OAuth2User oauth2User) {
    User oauthUser = OAuthUserFactory
            .getOAuth2UserInfo(oauth2UserRequest.getClientRegistration().getRegistrationId(),
                    oauth2User.getAttributes());

    String email = (String) oauthUser.getEmail();
    String provider = (String) oauthUser.getPassword();
    String regId = oauth2UserRequest.getClientRegistration().getRegistrationId();
    if (StringUtils.hasLength(email)) {
      throw new OAuthException("Email not found from OAuth2 provider");
    }

    User existingUser = userRepository.findByEmail(email);
    if (existingUser != null) {
      // User already exists with same email id.
      if (!provider.equals(regId)) {
        throw new OAuthException("Looks like you're signed up with " + provider
                + " account. Please use your " + provider + " account to login.");
      }

      // Check if user info is updated at provider
      boolean userUpdateRequired = userUpdateRequired(existingUser, oauthUser);
      if (userUpdateRequired) {
        // Update user in db
        userRepository.save(oauthUser);
      }
    } else {
      // User does not exist in db, so add the user to db.
      userRepository.save(oauthUser);
    }

    return CustomUserPrincipal.create(oauthUser, oauth2User.getAttributes());
  }

  private boolean userUpdateRequired(User existingUser, User oauthUser) {
    return !existingUser.getName()
            .equals(oauthUser.getName()) || !existingUser
            .getImageUrl().equals(oauthUser.getImageUrl());
  }
}
