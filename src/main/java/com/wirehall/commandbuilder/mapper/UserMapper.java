package com.wirehall.commandbuilder.mapper;

import com.wirehall.commandbuilder.dto.User;
import com.wirehall.commandbuilder.dto.auth.SignUp;
import com.wirehall.commandbuilder.model.props.USER_PROPERTY;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;


public class UserMapper extends BaseMapper {

    /**
     * @param userVertex User vertex to be converted to User dto
     * @return The User dto is returned, Only the User details are available in the dto
     */
    public Optional<User> mapToUser(Optional<Vertex> userVertex) {
        if (userVertex.isPresent()) {
            Vertex v = userVertex.get();
            User user = new User();
            user.setId(v.id());

            for (USER_PROPERTY userProperty : USER_PROPERTY.values()) {
                if (userProperty.isMandatory() || v.property(userProperty.toString()).isPresent()) {
                    user.addProperty(userProperty, v.property(userProperty.toString()).value());
                }
            }

            return Optional.of(user);
        }

        return Optional.empty();
    }

    /**
     * @param signUpRequest SignUp request used to create user dto
     * @return The User dto is returned, Only the User details are available in the dto
     */
    public User mapToUser(SignUp signUpRequest, PasswordEncoder passwordEncoder) {
        User user = new User();

        user.addProperty(USER_PROPERTY.name, signUpRequest.getName());
        user.addProperty(USER_PROPERTY.email, signUpRequest.getEmail());
        user.addProperty(USER_PROPERTY.emailVerified, false);
        user.addProperty(USER_PROPERTY.provider, User.OAUTH_PROVIDER.local.toString());
        user.addProperty(USER_PROPERTY.password, passwordEncoder.encode(signUpRequest.getPassword()));
        return user;
    }
}
