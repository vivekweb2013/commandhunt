package com.wirehall.commandbuilder.repository;

import com.wirehall.commandbuilder.dto.User;
import com.wirehall.commandbuilder.dto.auth.SignUp;
import com.wirehall.commandbuilder.mapper.UserMapper;
import com.wirehall.commandbuilder.model.VERTEX;
import com.wirehall.commandbuilder.model.props.USER_PROPERTY;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);
  private final UserMapper mapper = new UserMapper();
  private final JanusGraph graph;
  private final GraphTraversalSource g;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired
  public UserRepository(JanusGraph graph, GraphTraversalSource g) {
    this.graph = graph;
    this.g = g;
  }

  public Optional<User> findByEmail(String email) {
    g.tx().rollback();
    Optional<Vertex> user =
        g.V().hasLabel(VERTEX.user.toString()).has(USER_PROPERTY.email.toString(), email).tryNext();
    return mapper.mapToUser(user);
  }

  public Optional<User> findById(Object id) {
    g.tx().rollback();
    Optional<Vertex> user = g.V(id).tryNext();
    return mapper.mapToUser(user);
  }

  public Boolean existsByEmail(String email) {
    g.tx().rollback();
    return g.V()
        .hasLabel(VERTEX.user.toString())
        .has(USER_PROPERTY.email.toString(), email)
        .hasNext();
  }

  public User addUser(SignUp signUpRequest) {
    g.tx().rollback();
    User user = mapper.mapToUser(signUpRequest, passwordEncoder);

    GraphTraversal<Vertex, Vertex> graphTraversal = g.addV(VERTEX.user.toString());

    for (USER_PROPERTY property : USER_PROPERTY.values()) {
      if (user.getProperty(property) != null) {
        graphTraversal.property(property.toString(), user.getProperty(property));
      }
    }
    Vertex userVertex = graphTraversal.next();
    user.setId(userVertex.id());

    // Remove password field
    user.getProperties().remove(USER_PROPERTY.password);

    LOGGER.info(user.toString());
    g.tx().commit();
    return user;
  }
}
