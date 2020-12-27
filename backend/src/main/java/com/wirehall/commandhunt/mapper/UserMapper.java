package com.wirehall.commandhunt.mapper;

import com.wirehall.commandhunt.dto.User;
import com.wirehall.commandhunt.dto.User.OAuthProvider;
import com.wirehall.commandhunt.dto.auth.SignUp;
import com.wirehall.commandhunt.model.props.UserProperty;
import java.util.Optional;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper extends BaseMapper {

  /**
   * Maps the vertex to user dto.
   *
   * @param userVertex User vertex to be converted to user dto.
   * @return The User dto is returned, Only the User details are available in the dto.
   */
  public Optional<User> mapToUser(Optional<Vertex> userVertex) {
    if (userVertex.isPresent()) {
      Vertex v = userVertex.get();
      User user = new User();
      user.setId(v.id());

      for (UserProperty userProperty : UserProperty.values()) {
        if (userProperty.isMandatory() || v.property(userProperty.toLowerCase()).isPresent()) {
          user.addProperty(userProperty, v.property(userProperty.toLowerCase()).value());
        }
      }

      return Optional.of(user);
    }

    return Optional.empty();
  }

  /**
   * Maps the sign up request to user dto.
   *
   * @param signUpRequest SignUp request used to create user dto.
   * @return The User dto is returned, Only the User details are available in the dto.
   */
  public User mapToUser(SignUp signUpRequest, PasswordEncoder passwordEncoder) {
    User user = new User();

    user.addProperty(UserProperty.NAME, signUpRequest.getName());
    user.addProperty(UserProperty.EMAIL, signUpRequest.getEmail());
    user.addProperty(UserProperty.EMAIL_VERIFIED, false);
    user.addProperty(UserProperty.PROVIDER, OAuthProvider.local.toString());
    user.addProperty(UserProperty.PASSWORD, passwordEncoder.encode(signUpRequest.getPassword()));
    return user;
  }
}
