package com.wirehall.commandbuilder.repository;

import com.wirehall.commandbuilder.dto.User;
import com.wirehall.commandbuilder.dto.auth.SignUp;
import com.wirehall.commandbuilder.mapper.UserMapper;
import com.wirehall.commandbuilder.model.VertexType;
import com.wirehall.commandbuilder.model.props.UserProperty;
import java.util.Optional;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);
  private final UserMapper mapper = new UserMapper();
  private final GraphTraversalSource gt;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  public UserRepository(GraphTraversalSource gt) {
    this.gt = gt;
  }

  /**
   * Retrieves user by email address from the database.
   *
   * @param email Email address.
   * @return User DTO.
   */
  public Optional<User> findByEmail(String email) {
    gt.tx().rollback();

    Optional<Vertex> user = gt.V().hasLabel(VertexType.user.toString())
        .has(UserProperty.email.toString(), email).tryNext();

    return mapper.mapToUser(user);
  }

  /**
   * Checks if the user already exists with specifies email address in the database.
   *
   * @param email Email address.
   * @return true if user with specified email exists, false otherwise.
   */
  public Boolean existsByEmail(String email) {
    gt.tx().rollback();

    return gt.V().hasLabel(VertexType.user.toString())
        .has(UserProperty.email.toString(), email).hasNext();
  }

  /**
   * Creates and persists the user in the database using sign up request payload.
   *
   * @param signUpRequest Sign up request payload.
   * @return User DTO.
   */
  public User addUser(SignUp signUpRequest) {
    User user = mapper.mapToUser(signUpRequest, passwordEncoder);
    return addUser(user);
  }

  /**
   * Persist the user to database.
   *
   * @param user User DTO.
   * @return Persisted user DTO.
   */
  public User addUser(User user) {
    gt.tx().rollback();

    GraphTraversal<Vertex, Vertex> graphTraversal = gt.addV(VertexType.user.toString());

    for (UserProperty property : UserProperty.values()) {
      if (user.getProperty(property) != null) {
        graphTraversal.property(property.toString(), user.getProperty(property));
      }
    }
    Vertex userVertex = graphTraversal.next();
    user.setId(userVertex.id());

    // Remove password field
    user.removeProperty(UserProperty.password);

    gt.tx().commit();

    LOGGER.info("User successfully added in the database.");
    return user;
  }

  /**
   * Updates the user in database.
   *
   * @param user The user DTO to update.
   */
  public void updateUser(User user) {
    gt.tx().rollback();

    GraphTraversal<Vertex, Vertex> graphTraversal = gt.V().hasLabel(VertexType.user.toString())
        .has(UserProperty.email.toString(), user.getProperty(UserProperty.email));

    for (UserProperty property : UserProperty.values()) {
      if (user.getProperty(property) != null) {
        graphTraversal.property(property.toString(), user.getProperty(property));
      }
    }
    graphTraversal.next();
    gt.tx().commit();

    // Remove password field
    user.removeProperty(UserProperty.password);

    LOGGER.info("User successfully updated in the database.");
  }


}
