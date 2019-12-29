package com.wirehall.commandbuilder.mapper;

import com.wirehall.commandbuilder.dto.Command;
import com.wirehall.commandbuilder.graph.SchemaManager;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.ArrayList;
import java.util.List;

public final class MainMapper extends BaseMapper {

    public Command mapToCommand(Vertex commandVertex) {
        Command command = new Command();
        command.setId(commandVertex.id());
        command.getProperties().put(SchemaManager.PROPERTIES.desc.toString(), commandVertex.property(SchemaManager.PROPERTIES.desc.toString()).value());
        if (commandVertex.property(SchemaManager.PROPERTIES.long_desc.toString()).isPresent()) {
            command.addProperty(SchemaManager.PROPERTIES.long_desc.toString(), commandVertex.property(SchemaManager.PROPERTIES.long_desc.toString()).value());
        }
        return command;
    }

    public List<Command> mapToCommands(List<Vertex> commandVertexList) {
        List<Command> commands = new ArrayList<>();
        for (Vertex commandVertex : commandVertexList) {
            Command command = mapToCommand(commandVertex);
            commands.add(command);
        }
        return commands;
    }
}
