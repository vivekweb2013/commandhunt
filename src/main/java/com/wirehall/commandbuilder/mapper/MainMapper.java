package com.wirehall.commandbuilder.mapper;

import com.wirehall.commandbuilder.dto.Command;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.ArrayList;
import java.util.List;

public class MainMapper extends BaseMapper {
    public Command mapToCommand(Vertex command) {
        return new Command();
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
