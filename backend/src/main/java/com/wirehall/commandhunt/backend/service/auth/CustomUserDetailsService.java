package com.wirehall.commandhunt.backend.service.auth;

import com.wirehall.commandhunt.backend.model.UserEntity;
import com.wirehall.commandhunt.backend.model.auth.CustomUserPrincipal;
import com.wirehall.commandhunt.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The principal is always the result of the UserDetailsService that returned the object
 *
 * <p>NOTE: There is often some confusion about UserDetailsService. It is purely a DAO for user data
 * and performs no other function other than to supply that data to other components within the
 * framework. In particular, it does not authenticate the user, which is done by the
 * AuthenticationManager. In many cases it makes more sense to implement AuthenticationProvider
 * directly if you require a custom authentication process.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Returns the user instance by username (i.e. email). Email is used as the primary key for user
   * entity so username will always refer to the email.
   *
   * @param email The email address of user.
   * @return Returns the user.
   */
  @Override
  public UserDetails loadUserByUsername(String email) {
    Optional<UserEntity> userEntity = userRepository.findById(email);
    if (!userEntity.isPresent()) {
      throw new BadCredentialsException("No user exists with email : " + email);
    }

    return CustomUserPrincipal.create(userEntity.get());
  }
}
