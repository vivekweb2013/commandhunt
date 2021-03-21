package com.wirehall.commandhunt.backend.mapper;

import com.wirehall.commandhunt.backend.dto.User;
import com.wirehall.commandhunt.backend.dto.User.OAuthProvider;
import com.wirehall.commandhunt.backend.dto.auth.SignUp;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper extends BaseMapper {

  /**
   * Maps the sign up request to user dto.
   *
   * @param signUpRequest SignUp request used to create user dto.
   * @return The User dto is returned, Only the User details are available in the dto.
   */
  public User mapToUser(SignUp signUpRequest, PasswordEncoder passwordEncoder) {
    User user = new User();

    user.setName(signUpRequest.getName());
    user.setEmail(signUpRequest.getEmail());
    user.setEmailVerified(false);
    user.setProvider(OAuthProvider.local);
    user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
    return user;
  }
}
