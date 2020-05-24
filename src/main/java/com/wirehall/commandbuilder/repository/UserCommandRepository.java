package com.wirehall.commandbuilder.repository;


import com.wirehall.commandbuilder.dto.UserCommand;
import com.wirehall.commandbuilder.dto.filter.Filter;
import com.wirehall.commandbuilder.dto.filter.Page;
import com.wirehall.commandbuilder.dto.filter.Pageable;
import com.wirehall.commandbuilder.mapper.UserCommandMapper;
import com.wirehall.commandbuilder.model.EdgeType;
import com.wirehall.commandbuilder.model.VertexType;
import com.wirehall.commandbuilder.model.props.UserCommandProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Column;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.attribute.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserCommandRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserCommandRepository.class);

  private final UserCommandMapper mapper = new UserCommandMapper();
  private final GraphTraversalSource gt;

  @Autowired
  public UserCommandRepository(GraphTraversalSource gt) {
    this.gt = gt;
  }

  /**
   * Get all the user-commands.
   *
   * @return List of UserCommand DTOs This will return the list of all UserCommand DTOs.
   */
  public List<UserCommand> getAllUserCommands() {
    LOGGER.debug("Retrieving all user-commands");

    List<Vertex> vertices = gt.V().hasLabel(VertexType.USERCOMMAND.toLowerCase()).toList();
    List<UserCommand> userCommands = new ArrayList<>();
    for (Vertex userCommandsVertex : vertices) {
      UserCommand userCommand = mapper.mapToUserCommand(userCommandsVertex);
      userCommands.add(userCommand);
    }
    return userCommands;
  }

  /**
   * Get all the user-commands.
   *
   * @param filter Filter criteria.
   * @return Page of user-command DTOs.
   */
  public Page<UserCommand> getAllUserCommands(Filter filter) {
    LOGGER.debug("Retrieving all user-commands");
    LOGGER.debug("Applying the filter: {}", filter);

    Pageable pageable = filter.getPageable();
    Page<UserCommand> userCommandPage = new Page<>();

    Long totalSize = gt.V().hasLabel(VertexType.USERCOMMAND.toLowerCase()).count().next();

    List<Vertex> vertices = gt.V().hasLabel(VertexType.USERCOMMAND.toLowerCase())
        .order().by(pageable.getSort().getSortBy(),
            Order.valueOf(pageable.getSort().getSortOrder().toLowerCase()))
        .range(pageable.getOffset(), pageable.getOffset() + pageable.getPageSize())
        .toList();

    List<UserCommand> userCommands = new ArrayList<>();
    for (Vertex userCommandsVertex : vertices) {
      UserCommand userCommand = mapper.mapToUserCommand(userCommandsVertex);
      userCommands.add(userCommand);
    }

    userCommandPage.setTotalSize(totalSize);
    userCommandPage.setPageSize(pageable.getPageSize());
    userCommandPage.setRecords(userCommands);

    return userCommandPage;
  }

  /**
   * Retrieves the user-command matching with id.
   *
   * @param userCommandId The UserCommand id.
   * @return The user-command dto.
   */
  public UserCommand getUserCommandById(String userCommandId) {
    LOGGER.debug("Retrieving user-command by id: {}", userCommandId);

    Vertex userCommandVertex = gt.V(userCommandId).next();
    return getUserCommand(userCommandVertex);
  }

  private UserCommand getUserCommand(Vertex userCommandVertex) {
    UserCommand userCommand = mapper.mapToUserCommand(userCommandVertex);

    Map<String, Object> flagValueProps =
        gt.V(userCommandVertex)
            .outE()
            .hasLabel(EdgeType.HAS_FLAG_VALUE.toLowerCase())
            .as("E")
            .inV()
            .as("V")
            .select("E", "V")
            .by(__.valueMap()
                .unfold()
                .group()
                .by(Column.keys)
                .by(__.select(Column.values).unfold()))
            .next();

    Map<String, Object> flags = mapper.mapToUserCommandFlags(flagValueProps);
    userCommand.setFlags(flags);

    Map<String, Object> optionValueProps =
        gt.V(userCommandVertex)
            .outE()
            .hasLabel(EdgeType.HAS_OPTION_VALUE.toLowerCase())
            .as("E")
            .inV()
            .as("V")
            .select("E", "V")
            .by(__.valueMap()
                .unfold()
                .group()
                .by(Column.keys)
                .by(__.select(Column.values).unfold()))
            .next();

    Map<String, Object> options = mapper.mapToUserCommandOptions(optionValueProps);
    userCommand.setOptions(options);

    return userCommand;
  }

  public List<UserCommand> getMatchingUserCommands(Filter filter) {
    // TODO: To be implemented
    return new ArrayList<>();
  }

  /**
   * Filters and retrieves user-commands based on query argument.
   *
   * @param query The text to be matched against user-command.
   * @return List of all the matching user-command.
   */
  public List<UserCommand> getMatchingUserCommands(String query) {
    List<Vertex> vertices = gt.V().hasLabel(VertexType.USERCOMMAND.toLowerCase()).or(
        __.has(UserCommandProperty.COMMAND_TEXT.toLowerCase(), Text.textContainsFuzzy(query)),
        __.has(UserCommandProperty.COMMAND_NAME.toLowerCase(), Text.textContainsFuzzy(query)))
        .toList();

    List<UserCommand> commands = new ArrayList<>();
    for (Vertex userCommandVertex : vertices) {
      UserCommand userCommand = mapper.mapToUserCommand(userCommandVertex);
      commands.add(userCommand);
    }

    return commands;
  }

  /**
   * Adds the user-command to database.
   *
   * @param userCommand The user-command dto to add to database.
   */
  public void addUserCommand(UserCommand userCommand) {
    gt.tx().rollback();

    GraphTraversal<Vertex, Vertex> graphTraversal = gt.addV(VertexType.USERCOMMAND.toLowerCase());

    for (UserCommandProperty property : UserCommandProperty.values()) {
      if (userCommand.getProperty(property) != null) {
        graphTraversal.property(property.toLowerCase(), userCommand.getProperty(property));
      }
    }
    Vertex userCommandVertex = graphTraversal.next();
    addFlagValue(userCommandVertex, userCommand.getFlags());
    addOptionValue(userCommandVertex, userCommand.getOptions());

    gt.tx().commit();
  }

  private void addFlagValue(Vertex userCommandVertex, Map<String, Object> flags) {
    Vertex flagValueVertex = gt.addV(VertexType.FLAGVALUE.toLowerCase()).next();
    GraphTraversal<Vertex, Vertex> vertexGraphTraversal = gt.V(flagValueVertex);
    GraphTraversal<Vertex, Edge> edgeGraphTraversal = gt.V(userCommandVertex).as("a")
        .V(flagValueVertex).addE(EdgeType.HAS_FLAG_VALUE.toLowerCase());

    for (Entry<String, Object> entry : flags.entrySet()) {
      vertexGraphTraversal.property(entry.getKey(), entry.getValue());
    }

    vertexGraphTraversal.next();
    edgeGraphTraversal.from("a").next();
  }

  private void updateFlagValue(Vertex userCommandVertex, Map<String, Object> flags) {
    GraphTraversal<Vertex, Vertex> graphTraversal = gt.V(userCommandVertex).outE()
        .hasLabel(EdgeType.HAS_FLAG_VALUE.toLowerCase()).inV();

    for (Entry<String, Object> entry : flags.entrySet()) {
      graphTraversal.property(entry.getKey(), entry.getValue());
    }

    graphTraversal.next();
  }

  private void addOptionValue(Vertex userCommandVertex, Map<String, Object> options) {
    Vertex optionValueVertex = gt.addV(VertexType.OPTIONVALUE.toLowerCase()).next();
    GraphTraversal<Vertex, Vertex> vertexGraphTraversal = gt.V(optionValueVertex);
    GraphTraversal<Vertex, Edge> edgeGraphTraversal = gt.V(userCommandVertex).as("a")
        .V(optionValueVertex).addE(EdgeType.HAS_OPTION_VALUE.toLowerCase());

    for (Entry<String, Object> entry : options.entrySet()) {
      vertexGraphTraversal.property(entry.getKey(), entry.getValue());
    }

    vertexGraphTraversal.next();
    edgeGraphTraversal.from("a").next();
  }

  private void updateOptionValue(Vertex userCommandVertex, Map<String, Object> options) {
    GraphTraversal<Vertex, Vertex> graphTraversal = gt.V(userCommandVertex).outE()
        .hasLabel(EdgeType.HAS_OPTION_VALUE.toLowerCase()).inV();

    for (Entry<String, Object> entry : options.entrySet()) {
      graphTraversal.property(entry.getKey(), entry.getValue());
    }

    graphTraversal.next();
  }

  /**
   * Updates the user-command in the database.
   *
   * @param userCommand The user-command dto to be updated.
   */
  public void updateUserCommand(UserCommand userCommand) {
    gt.tx().rollback();

    GraphTraversal<Vertex, Vertex> graphTraversal = gt.V(userCommand.getId());

    for (UserCommandProperty property : UserCommandProperty.values()) {
      if (userCommand.getProperty(property) != null) {
        graphTraversal.property(property.toLowerCase(), userCommand.getProperty(property));
      }
    }
    Vertex userCommandVertex = graphTraversal.next();
    updateFlagValue(userCommandVertex, userCommand.getFlags());
    updateOptionValue(userCommandVertex, userCommand.getOptions());

    gt.tx().commit();
  }

  /**
   * Deletes the user-command from database.
   *
   * @param userCommandId The id of user-command to be deleted.
   */
  public void deleteUserCommand(String userCommandId) {
    gt.tx().rollback();
    gt.V(userCommandId).next().remove();
    gt.tx().commit();
  }
}
