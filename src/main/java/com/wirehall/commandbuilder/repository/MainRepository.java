package com.wirehall.commandbuilder.repository;

import com.wirehall.commandbuilder.dto.Command;
import com.wirehall.commandbuilder.dto.Filter;
import com.wirehall.commandbuilder.mapper.MainMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MainRepository {
    private MainMapper mapper = new MainMapper();

    public List<Command> getAllCommands() {

        List<Command> commands = new ArrayList<>();
        return commands;
    }

    public Command getCommandById(String id) {
        Command command = new Command();
        return command;
    }

    public Command getCommandByName(String name) {
        Command command = new Command();
        return command;
    }

    public List<Command> getCommandsByFilter(Filter filter) {
        List<Command> commands = new ArrayList<>();
        return commands;
    }
}
