package com.wirehall.commandbuilder.mapper;

import com.wirehall.commandbuilder.dto.Command;
import com.wirehall.commandbuilder.dto.Flag;
import com.wirehall.commandbuilder.dto.Option;
import com.wirehall.commandbuilder.model.props.CommandProperty;
import com.wirehall.commandbuilder.model.props.FlagProperty;
import com.wirehall.commandbuilder.model.props.OptionProperty;
import java.util.Map;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public final class CommandMapper extends BaseMapper {

  /**
   * Used to map the vertex to command dto.
   *
   * @param commandVertex Command vertex to be converted to Command dto.
   * @return The Command dto is returned, Only the Command details are available in the dto.
   */
  public Command mapToCommand(Vertex commandVertex) {
    Command command = new Command();
    command.setId(commandVertex.id());

    for (CommandProperty commandProperty : CommandProperty.values()) {
      if (commandProperty.isMandatory()
          || commandVertex.property(commandProperty.toLowerCase()).isPresent()) {
        command.addProperty(
            commandProperty, commandVertex.property(commandProperty.toLowerCase()).value());
      }
    }
    return command;
  }

  /**
   * Used to map the vertex to Flag dto.
   *
   * @param flagVertexProps Properties of flag's vertex.
   * @param flagEdgeProps   Properties of flag's edge.
   * @return Flag dto.
   */
  public Flag mapToFlag(Map<Object, Object> flagVertexProps, Map<Object, Object> flagEdgeProps) {
    Flag flag = new Flag();

    for (FlagProperty flagProperty : FlagProperty.values()) {
      if (flagProperty.isMandatory()
          || flagVertexProps.containsKey(flagProperty.toLowerCase())
          || flagEdgeProps.containsKey(flagProperty.toLowerCase())) {

        if (flagProperty.propertyOf().equals("V")) {
          flag.addProperty(flagProperty, flagVertexProps.get(flagProperty.toLowerCase()));
        } else if (flagProperty.propertyOf().equals("E")) {
          flag.addProperty(flagProperty, flagEdgeProps.get(flagProperty.toLowerCase()));
        }
      }
    }

    flag.setId(flagVertexProps.get(T.id));
    return flag;
  }

  /**
   * Used to map the vertex to option dto.
   *
   * @param optionVertexProps Properties of options's vertex.
   * @param optionEdgeProps   Properties of options's edge.
   * @return Option dto.
   */
  public Option mapToOption(
      Map<Object, Object> optionVertexProps, Map<Object, Object> optionEdgeProps) {
    Option option = new Option();

    for (OptionProperty optionProperty : OptionProperty.values()) {
      if (optionProperty.isMandatory()
          || optionVertexProps.containsKey(optionProperty.toLowerCase())
          || optionEdgeProps.containsKey(optionProperty.toLowerCase())) {

        if (optionProperty.propertyOf().equals("V")) {
          option.addProperty(optionProperty, optionVertexProps.get(optionProperty.toLowerCase()));
        } else if (optionProperty.propertyOf().equals("E")) {
          option.addProperty(optionProperty, optionEdgeProps.get(optionProperty.toLowerCase()));
        }
      }
    }

    option.setId(optionVertexProps.get(T.id));
    return option;
  }
}
