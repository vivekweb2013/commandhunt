package com.wirehall.commandbuilder.mapper;

import com.wirehall.commandbuilder.dto.Command;
import com.wirehall.commandbuilder.dto.Flag;
import com.wirehall.commandbuilder.dto.Option;
import com.wirehall.commandbuilder.model.props.COMMAND_PROPERTY;
import com.wirehall.commandbuilder.model.props.FLAG_PROPERTY;
import com.wirehall.commandbuilder.model.props.OPTION_PROPERTY;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class CommandMapper extends BaseMapper {

    /**
     * @param commandVertex Command vertex to be converted to Command dto
     * @return The Command dto is returned, Only the Command details are available in the dto
     */
    public Command mapToCommand(Vertex commandVertex) {
        Command command = new Command();
        command.setId(commandVertex.id());

        for (COMMAND_PROPERTY commandProperty : COMMAND_PROPERTY.values()) {
            if (commandProperty.isMandatory() || commandVertex.property(commandProperty.toString()).isPresent()) {
                command.addProperty(commandProperty, commandVertex.property(commandProperty.toString()).value());
            }
        }
        return command;
    }

    private Flag mapToFlag(Vertex flagVertex) {
        Flag flag = new Flag();
        flag.setId(flagVertex.id());
        for (FLAG_PROPERTY flagProperty : FLAG_PROPERTY.values()) {
            if (flagProperty.isMandatory() || flagVertex.property(flagProperty.toString()).isPresent()) {
                flag.addProperty(flagProperty, flagVertex.property(flagProperty.toString()).value());
            }
        }
        return flag;
    }

    public Flag mapToFlag(Map<Object, Object> flagVertexProps, Map<Object, Object> flagEdgeProps) {
        Flag flag = new Flag();

        for (FLAG_PROPERTY flagProperty : FLAG_PROPERTY.values()) {
            if (flagProperty.isMandatory() || flagVertexProps.containsKey(flagProperty.toString()) ||
                    flagEdgeProps.containsKey(flagProperty.toString())) {

                if (flagProperty.propertyOf().equals("V")) {
                    flag.addProperty(flagProperty, flagVertexProps.get(flagProperty.toString()));
                } else if (flagProperty.propertyOf().equals("E")) {
                    flag.addProperty(flagProperty, flagEdgeProps.get(flagProperty.toString()));
                }
            }
        }

        flag.setId(flagVertexProps.get(T.id));
        return flag;
    }

    public Option mapToOption(Map<Object, Object> optionVertexProps, Map<Object, Object> optionEdgeProps) {
        Option option = new Option();

        for (OPTION_PROPERTY optionProperty : OPTION_PROPERTY.values()) {
            if (optionProperty.isMandatory() || optionVertexProps.containsKey(optionProperty.toString()) ||
                    optionEdgeProps.containsKey(optionProperty.toString())) {

                if (optionProperty.propertyOf().equals("V")) {
                    option.addProperty(optionProperty, optionVertexProps.get(optionProperty.toString()));
                } else if (optionProperty.propertyOf().equals("E")) {
                    option.addProperty(optionProperty, optionEdgeProps.get(optionProperty.toString()));
                }
            }
        }

        option.setId(optionVertexProps.get(T.id));
        return option;
    }

    public List<Flag> mapToFlag(List<Vertex> flagVertexList) {
        List<Flag> flags = new ArrayList<>();
        for (Vertex flagVertex : flagVertexList) {
            Flag flag = mapToFlag(flagVertex);
            flags.add(flag);
        }
        return flags;
    }

    private Option mapToOption(Vertex optionVertex) {
        Option option = new Option();
        option.setId(optionVertex.id());
        for (OPTION_PROPERTY optionProperty : OPTION_PROPERTY.values()) {
            if (optionProperty.isMandatory() || optionVertex.property(optionProperty.toString()).isPresent()) {
                option.addProperty(optionProperty, optionVertex.property(optionProperty.toString()).value());
            }
        }
        return option;
    }

    public List<Option> mapToOptions(List<Vertex> optionVertexList) {
        List<Option> options = new ArrayList<>();
        for (Vertex optionVertex : optionVertexList) {
            Option option = mapToOption(optionVertex);
            options.add(option);
        }
        return options;
    }


}
