package com.wirehall.commandbuilder.graph.commands;

import com.wirehall.commandbuilder.dto.Command;
import com.wirehall.commandbuilder.repository.MainRepository;
import com.wirehall.commandbuilder.util.NodeBuilder;

public class RM {
    public static void createData(MainRepository repo) {
        Command c = NodeBuilder.buildCommand("rm", "Remove Files or Directories", "rm removes each specified file. By default, it does not remove directories.");

        NodeBuilder.addFlag(c, "f", "--force", "-", "Force", "ignore nonexistent files, never prompt", 1);
        NodeBuilder.addFlag(c, "i", null, "-", "Interactive", "prompt before every removal", 1);
        NodeBuilder.addFlag(c, "I", null, "-", "Less Interactive", "prompt once before removing more than three files, or when removing recursively. Less intrusive than -i, while still giving protection against most mistakes", 1);
        NodeBuilder.addFlag(c, "no-preserve-root", null, "--", "No Preserve Root", "do not treat '/' specially", 1);
        NodeBuilder.addFlag(c, "preserve-root", null, "--", "Preserve Root", "do not remove '/' (default)", 1);
        NodeBuilder.addFlag(c, "r", "-R, --recursive", "--", "Recursive", "remove directories and their contents recursively", 1);
        NodeBuilder.addFlag(c, "v", "--verbose", "--", "Verbose", "explain what is being done", 1);

        repo.addCommand(c);
    }
}
