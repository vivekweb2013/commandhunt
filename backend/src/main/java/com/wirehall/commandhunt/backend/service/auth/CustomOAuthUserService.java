package com.wirehall.commandhunt.backend.service.auth;


import com.wirehall.commandhunt.backend.exception.OAuthException;
import com.wirehall.commandhunt.backend.model.UserEntity;
import com.wirehall.commandhunt.backend.model.auth.CustomUserPrincipal;
import com.wirehall.commandhunt.backend.repository.UserRepository;
import com.wirehall.commandhunt.backend.security.OAuthUserFactory;
import java.sql.Timestamp;
import java.util.Optional;
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
    UserEntity oauthUser = OAuthUserFactory
        .getOAuth2UserInfo(oauth2UserRequest.getClientRegistration().getRegistrationId(),
            oauth2User.getAttributes());

    String email = oauthUser.getEmail();
    String provider = String.valueOf(oauthUser.getProvider());
    String regId = oauth2UserRequest.getClientRegistration().getRegistrationId();
    if (!StringUtils.hasLength(email)) {
      throw new OAuthException("Email not found from OAuth2 provider");
    }

    Optional<UserEntity> existingUser = userRepository.findById(email);
    if (existingUser.isPresent()) {
      // User already exists with same email id.
      if (!provider.equalsIgnoreCase(regId)) {
        throw new OAuthException("Looks like you're signed up with " + provider
            + " account. Please use your " + provider + " account to login.");
      }

      // Check if user info is updated at provider
      boolean userUpdateRequired = userUpdateRequired(existingUser.get(), oauthUser);
      if (userUpdateRequired) {
        oauthUser.setJoinedOn(existingUser.get().getJoinedOn());
        // Update user in db
        userRepository.save(oauthUser);
      }
    } else {
      // User does not exist in db, so add the user to db.
      oauthUser.setJoinedOn(new Timestamp(System.currentTimeMillis()));
      userRepository.save(oauthUser);
    }

    return CustomUserPrincipal.create(oauthUser, oauth2User.getAttributes());
  }

  private boolean userUpdateRequired(UserEntity existingUser, UserEntity oauthUser) {
    return !existingUser.getName()
        .equals(oauthUser.getName()) || !existingUser
        .getImageUrl().equals(oauthUser.getImageUrl());
  }
}
