package com.wirehall.commandbuilder.graph.commands;

import com.wirehall.commandbuilder.dto.Command;
import com.wirehall.commandbuilder.repository.MainRepository;
import com.wirehall.commandbuilder.util.NodeBuilder;

public class VI {
    public static void createData(MainRepository repo) {
        Command c = NodeBuilder.buildCommand("vi", "Visual Display Editor", null);

        repo.addCommand(c);
    }
}
