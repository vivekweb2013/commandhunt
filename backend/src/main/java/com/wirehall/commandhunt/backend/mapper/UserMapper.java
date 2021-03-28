package com.wirehall.commandhunt.backend.mapper;

import com.wirehall.commandhunt.backend.dto.User;
import com.wirehall.commandhunt.backend.dto.auth.SignUp;
import com.wirehall.commandhunt.backend.model.UserEntity;
import com.wirehall.commandhunt.backend.model.UserEntity.OAuthProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {

  /**
   * Maps the sign up request to user entity.
   *
   * @param signUpRequest SignUp request used to create user dto.
   * @return User entity for jpa operations.
   */
  public UserEntity mapToUserEntity(SignUp signUpRequest, PasswordEncoder passwordEncoder) {
    UserEntity userEntity = new UserEntity();

    userEntity.setName(signUpRequest.getName());
    userEntity.setEmail(signUpRequest.getEmail());
    userEntity.setEmailVerified(false);
    userEntity.setProvider(OAuthProvider.LOCAL);
    userEntity.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
    return userEntity;
  }

  /**
   * Maps the user entity object to user dto object.
   *
   * @param userEntity User entity from jpa operations.
   * @return User dto object for sending response.
   */
  public User mapToUser(UserEntity userEntity) {
    User user = new User();

    user.setName(userEntity.getName());
    user.setEmail(userEntity.getEmail());
    user.setEmailVerified(userEntity.isEmailVerified());
    user.setProvider(userEntity.getProvider().toString());
    user.setProviderId(userEntity.getProviderId());
    user.setImageUrl(userEntity.getImageUrl());
    return user;
  }
}
