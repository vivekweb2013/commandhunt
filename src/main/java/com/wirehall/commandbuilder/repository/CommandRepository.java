package com.wirehall.commandbuilder.repository;

import com.wirehall.commandbuilder.dto.Command;
import com.wirehall.commandbuilder.dto.Flag;
import com.wirehall.commandbuilder.dto.Option;
import com.wirehall.commandbuilder.dto.filter.Filter;
import com.wirehall.commandbuilder.dto.filter.Page;
import com.wirehall.commandbuilder.dto.filter.Pageable;
import com.wirehall.commandbuilder.mapper.CommandMapper;
import com.wirehall.commandbuilder.model.EdgeType;
import com.wirehall.commandbuilder.model.VertexType;
import com.wirehall.commandbuilder.model.props.CommandProperty;
import com.wirehall.commandbuilder.model.props.FlagProperty;
import com.wirehall.commandbuilder.model.props.OptionProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.apache.tinkerpop.gremlin.structure.Column;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.attribute.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommandRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(CommandRepository.class);

  private final CommandMapper mapper = new CommandMapper();
  private final GraphTraversalSource gt;

  @Autowired
  public CommandRepository(GraphTraversalSource gt) {
    this.gt = gt;
  }

  /**
   * Get all the commands.
   *
   * @return List of command DTOs This will return the list of all command DTOs.
   */
  public List<Command> getAllCommands() {
    LOGGER.debug("Retrieving all commands from database.");

    List<Vertex> vertices = gt.V().hasLabel(VertexType.COMMAND.toLowerCase()).toList();
    List<Command> commands = new ArrayList<>();
    for (Vertex commandVertex : vertices) {
      Command command = mapper.mapToCommand(commandVertex);
      commands.add(command);
    }

    return commands;
  }

  /**
   * Get all the commands.
   *
   * @param filter Filter criteria.
   * @return List of command DTOs This will return the list of all command DTOs.
   */
  public Page<Command> getAllCommands(Filter filter) {
    LOGGER.debug("Retrieving all commands from database.");
    LOGGER.debug("Applying the filter: {}", filter);

    Pageable pageable = filter.getPageable();
    Page<Command> commandPage = new Page<>();

    Long totalSize = gt.V().hasLabel(VertexType.COMMAND.toLowerCase()).count().next();

    List<Vertex> vertices = gt.V().hasLabel(VertexType.COMMAND.toLowerCase())
        .range(pageable.getOffset(), pageable.getOffset() + pageable.getPageSize()).order()
        .by(pageable.getSort().getSortBy(),
            Order.valueOf(pageable.getSort().getSortOrder().toLowerCase()))
        .toList();
    List<Command> commands = new ArrayList<>();
    for (Vertex commandVertex : vertices) {
      Command command = mapper.mapToCommand(commandVertex);
      commands.add(command);
    }

    commandPage.setTotalSize(totalSize);
    commandPage.setPageSize(pageable.getPageSize());
    commandPage.setRecords(commands);

    return commandPage;
  }

  /**
   * Used to retrieve the command matching the id.
   *
   * @param id The command id.
   * @return The command DTO This will return the command DTO with the matching id.
   */
  public Command getCommandById(String id) {
    LOGGER.debug("Retrieving command with id: {}", id);

    Vertex commandVertex = gt.V(id).next();
    return getCommand(commandVertex);
  }

  /**
   * Used to retrieve the command matching the name.
   *
   * @param name The command name.
   * @return The command DTO This will return the command DTO with the matching name.
   */
  public Command getCommandByName(String name) {
    LOGGER.debug("Retrieving command with name: {}", name);

    Vertex commandVertex = gt.V().hasLabel(VertexType.COMMAND.toLowerCase()).has("name", name)
        .next();
    return getCommand(commandVertex);
  }

  private Command getCommand(Vertex commandVertex) {
    Command command = mapper.mapToCommand(commandVertex);

    List<Map<String, Object>> flagList = gt.V(commandVertex)
        .outE()
        .hasLabel(EdgeType.HAS_FLAG.toLowerCase())
        .as("E")
        .inV()
        .as("V")
        .select("E", "V")
        .by(__.valueMap()
            .with(WithOptions.tokens)
            .unfold()
            .group()
            .by(Column.keys)
            .by(__.select(Column.values).unfold()))
        .toList();

    for (Map<String, Object> flagProps : flagList) {
      Map<Object, Object> flagVertexProps = (Map<Object, Object>) flagProps.get("V");
      Map<Object, Object> flagEdgeProps = (Map<Object, Object>) flagProps.get("E");
      Flag flag = mapper.mapToFlag(flagVertexProps, flagEdgeProps);
      command.addFlag(flag);
    }

    List<Map<String, Object>> optionList = gt.V(commandVertex)
        .outE()
        .hasLabel(EdgeType.HAS_OPTION.toLowerCase())
        .as("E")
        .inV()
        .as("V")
        .select("E", "V")
        .by(__.valueMap()
            .with(WithOptions.tokens)
            .unfold()
            .group()
            .by(Column.keys)
            .by(__.select(Column.values).unfold()))
        .toList();

    for (Map<String, Object> optionProps : optionList) {
      Map<Object, Object> optionVertexProps = (Map<Object, Object>) optionProps.get("V");
      Map<Object, Object> optionEdgeProps = (Map<Object, Object>) optionProps.get("E");
      Option option = mapper.mapToOption(optionVertexProps, optionEdgeProps);
      command.addOption(option);
    }
    return command;
  }

  public List<Command> getMatchingCommands(Filter filter) {
    // TODO: To be implemented.
    return new ArrayList<>();
  }

  /**
   * This is used to filter retrieve commands based on query argument.
   *
   * @param query The text to be matched against command.
   * @return List of all the matching command.
   */
  public List<Command> getMatchingCommands(String query) {
    LOGGER.debug("Retrieving matching commands by query: {}", query);

    List<Vertex> vertices =
        gt.V().hasLabel(VertexType.COMMAND.toLowerCase())
            .or(__.has(CommandProperty.NAME.toLowerCase(), Text.textContainsFuzzy(query)),
                __.has(CommandProperty.DESC.toLowerCase(), Text.textContainsFuzzy(query)),
                __.has(CommandProperty.LONG_DESC.toLowerCase(), Text.textContainsFuzzy(query)))
            .toList();

    List<Command> commands = new ArrayList<>();
    for (Vertex commandVertex : vertices) {
      Command command = mapper.mapToCommand(commandVertex);
      commands.add(command);
    }

    return commands;
  }

  /**
   * The command DTO Used to add the command to database.
   *
   * @param command he command to add to database.
   */
  public void addCommand(Command command) {
    LOGGER.trace("Adding command: {}", command);

    GraphTraversal<Vertex, Vertex> graphTraversal = gt.addV(VertexType.COMMAND.toLowerCase());

    for (CommandProperty property : CommandProperty.values()) {
      if (command.getProperty(property) != null) {
        graphTraversal.property(property.toLowerCase(), command.getProperty(property));
      }
    }
    Vertex commandVertex = graphTraversal.next();

    for (Flag flag : command.getFlags()) {
      addFlag(commandVertex, flag);
    }

    for (Option option : command.getOptions()) {
      addOption(commandVertex, option);
    }
  }

  private void addFlag(Vertex commandVertex, Flag flag) {
    Vertex flagVertex = gt.addV(VertexType.FLAG.toLowerCase()).next();
    GraphTraversal<Vertex, Vertex> vertexGraphTraversal = gt.V(flagVertex);
    GraphTraversal<Vertex, org.apache.tinkerpop.gremlin.structure.Edge> edgeGraphTraversal =
        gt.V(commandVertex).as("a").V(flagVertex).addE(EdgeType.HAS_FLAG.toLowerCase());

    for (FlagProperty property : FlagProperty.values()) {
      if (flag.getProperty(property) != null && property.propertyOf().equals("V")) {
        vertexGraphTraversal.property(property.toLowerCase(), flag.getProperty(property));
      } else if (flag.getProperty(property) != null && property.propertyOf().equals("E")) {
        edgeGraphTraversal.property(property.toLowerCase(), flag.getProperty(property));
      }
    }
    vertexGraphTraversal.next();
    edgeGraphTraversal.from("a").next();
  }

  private void addOption(Vertex commandVertex, Option option) {
    Vertex optionVertex = gt.addV(VertexType.OPTION.toLowerCase()).next();
    GraphTraversal<Vertex, Vertex> vertexGraphTraversal = gt.V(optionVertex);
    GraphTraversal<Vertex, org.apache.tinkerpop.gremlin.structure.Edge> edgeGraphTraversal =
        gt.V(commandVertex).as("a").V(optionVertex).addE(EdgeType.HAS_OPTION.toLowerCase());

    for (OptionProperty property : OptionProperty.values()) {
      if (option.getProperty(property) != null && property.propertyOf().equals("V")) {
        vertexGraphTraversal.property(property.toLowerCase(), option.getProperty(property));
      } else if (option.getProperty(property) != null && property.propertyOf().equals("E")) {
        edgeGraphTraversal.property(property.toLowerCase(), option.getProperty(property));
      }
    }
    vertexGraphTraversal.next();
    edgeGraphTraversal.from("a").next();
  }
}
