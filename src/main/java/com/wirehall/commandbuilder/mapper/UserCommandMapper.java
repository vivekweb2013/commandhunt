package com.wirehall.commandbuilder.mapper;


import com.wirehall.commandbuilder.dto.UserCommand;
import com.wirehall.commandbuilder.model.props.UserCommandProperty;
import java.util.HashMap;
import java.util.Map;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public final class UserCommandMapper extends BaseMapper {

  /**
   * Maps the vertex to UserCommand dto.
   *
   * @param userCommandVertex UserCommand vertex to be converted to UserCommand dto.
   * @return The UserCommand dto is returned, Only the UserCommand details are available in the dto.
   */
  public UserCommand mapToUserCommand(Vertex userCommandVertex) {
    UserCommand userCommand = new UserCommand();
    userCommand.setId(userCommandVertex.id());

    for (UserCommandProperty userCommandProperty : UserCommandProperty.values()) {
      if (userCommandProperty.isMandatory()
          || userCommandVertex.property(userCommandProperty.toLowerCase()).isPresent()) {
        userCommand.addProperty(userCommandProperty,
            userCommandVertex.property(userCommandProperty.toLowerCase()).value());
      }
    }
    return userCommand;
  }

  /**
   * Maps vertex properties of flag-value vertex to key-value map.
   *
   * @param flagValueProps Vertex properties of flag-value vertex.
   * @return Properties as key value pairs.
   */
  public Map<String, Object> mapToUserCommandFlags(Map<String, Object> flagValueProps) {
    Map<String, Object> flags = new HashMap<>();
    Map<String, Object> vertexProps = (Map<String, Object>) flagValueProps.get("V");
    Map<String, Object> edgeProps = (Map<String, Object>) flagValueProps.get("E");
    flags.putAll(vertexProps);
    flags.putAll(edgeProps);
    return flags;
  }

  /**
   * Maps vertex properties of flag-value vertex to key-value map.
   *
   * @param optionValueProps Vertex properties of option-value vertex.
   * @return Properties as key value pairs.
   */
  public Map<String, Object> mapToUserCommandOptions(Map<String, Object> optionValueProps) {
    Map<String, Object> options = new HashMap<>();
    Map<String, Object> vertexProps = (Map<String, Object>) optionValueProps.get("V");
    Map<String, Object> edgeProps = (Map<String, Object>) optionValueProps.get("E");
    options.putAll(vertexProps);
    options.putAll(edgeProps);
    return options;
  }
}
