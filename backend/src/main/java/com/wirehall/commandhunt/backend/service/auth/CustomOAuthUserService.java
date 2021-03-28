package com.wirehall.commandhunt.backend.service.auth;


import com.wirehall.commandhunt.backend.exception.OAuthException;
import com.wirehall.commandhunt.backend.model.UserEntity;
import com.wirehall.commandhunt.backend.model.UserEntity.OAuthProvider;
import com.wirehall.commandhunt.backend.model.auth.CustomUserPrincipal;
import com.wirehall.commandhunt.backend.repository.UserRepository;
import com.wirehall.commandhunt.backend.security.OAuthUserFactory;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class CustomOAuthUserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  @Autowired
  public CustomOAuthUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oauthUserRequest) {
    OAuth2User oauthUser = super.loadUser(oauthUserRequest);
    return processOAuth2User(oauthUserRequest, oauthUser);
  }

  private OAuth2User processOAuth2User(OAuth2UserRequest oauth2UserRequest, OAuth2User oauth2User) {
    try {
      String oAuthProviderString = oauth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase();
      if (!EnumUtils.isValidEnum(UserEntity.OAuthProvider.class, oAuthProviderString)) {
        throw new OAuthException("Sorry! Login with " + oAuthProviderString + " is not supported.");
      }
      OAuthProvider oAuthProvider = OAuthProvider.valueOf(oAuthProviderString);
      UserEntity oAuthUserEntity = OAuthUserFactory.getOAuth2UserInfo(oAuthProvider, oauth2User.getAttributes());

      String userEmail = oAuthUserEntity.getEmail();
      if (!StringUtils.hasLength(userEmail)) {
        throw new OAuthException("Can't retrieve email from " + oAuthProviderString + " OAuth provider");
      }
      syncUserWithDatabase(oAuthProvider, oAuthUserEntity, userEmail);

      return CustomUserPrincipal.create(oAuthUserEntity, oauth2User.getAttributes());
    } catch (AuthenticationException ex) {
      throw ex;
    } catch (Exception ex) {
      // Throwing an instance of AuthenticationException,
      // This will trigger the OAuthFailureHandler
      throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
    }
  }

  private void syncUserWithDatabase(OAuthProvider oAuthProvider, UserEntity oAuthUserEntity, String userEmail) {
    Optional<UserEntity> existingUserOptional = userRepository.findById(userEmail);
    if (existingUserOptional.isPresent()) {
      UserEntity existingUser = existingUserOptional.get();
      // User already exists with same email id.
      if (!oAuthProvider.equals(existingUser.getProvider())) {
        throw new OAuthException("Looks like you've already signed up with " + existingUser.getProvider()
                + " account. Please use your " + existingUser.getProvider() + " account to login.");
      }

      // Check if user info is updated at provider
      boolean userUpdateRequired = userUpdateRequired(existingUser, oAuthUserEntity);
      if (userUpdateRequired) {
        oAuthUserEntity.setJoinedOn(existingUser.getJoinedOn());
        // Update user in db
        userRepository.save(oAuthUserEntity);
      }
    } else {
      // User does not exist in db, so add the user to db.
      oAuthUserEntity.setJoinedOn(new Timestamp(System.currentTimeMillis()));
      userRepository.save(oAuthUserEntity);
    }
  }

  private boolean userUpdateRequired(UserEntity existingUser, UserEntity oauthUser) {
    return !existingUser.getName()
        .equals(oauthUser.getName()) || !existingUser
        .getImageUrl().equals(oauthUser.getImageUrl());
  }
}
