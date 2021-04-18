package com.wirehall.commandhunt.backend.mapper;

import com.wirehall.commandhunt.backend.dto.Flag;
import com.wirehall.commandhunt.backend.dto.MetaCommand;
import com.wirehall.commandhunt.backend.dto.Option;
import com.wirehall.commandhunt.backend.model.graph.props.FlagProperty;
import com.wirehall.commandhunt.backend.model.graph.props.MetaCommandProperty;
import com.wirehall.commandhunt.backend.model.graph.props.OptionProperty;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.Map;

public final class MetaCommandMapper {

  /**
   * Used to map the vertex to command dto.
   *
   * @param commandVertex MetaCommand vertex to be converted to MetaCommand dto.
   * @return The MetaCommand dto is returned, Only the MetaCommand details are available in the dto.
   */
  public MetaCommand mapToCommand(Vertex commandVertex) {
    MetaCommand metaCommand = new MetaCommand();
    metaCommand.setId(commandVertex.id());

    for (MetaCommandProperty metaCommandProperty : MetaCommandProperty.values()) {
      if (metaCommandProperty.isMandatory()
          || commandVertex.property(metaCommandProperty.toLowerCase()).isPresent()) {
        metaCommand.addProperty(
            metaCommandProperty, commandVertex.property(metaCommandProperty.toLowerCase()).value());
      }
    }
    return metaCommand;
  }

  /**
   * Used to map the vertex to Flag dto.
   *
   * @param flagVertexProps Properties of flag's vertex.
   * @param flagEdgeProps Properties of flag's edge.
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
   * @param optionEdgeProps Properties of options's edge.
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
