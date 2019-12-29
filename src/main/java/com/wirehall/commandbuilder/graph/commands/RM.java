package com.wirehall.commandbuilder.graph.commands;

import com.wirehall.commandbuilder.repository.MainRepository;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class RM {
    public static void createData(MainRepository m) {
        Vertex command = m.addCommand("rm", "Remove Files or Directories", "rm removes each specified file. By default, it does not remove directories.");
        createFlags(m, command);
        createOptions(m, command);
    }

    private static void createFlags(MainRepository m, Vertex command) {
        m.addFlag(command, "f", "--force", "-", "Force", "ignore nonexistent files, never prompt", 1);
        m.addFlag(command, "i", null, "-", "Interactive", "prompt before every removal", 1);
        m.addFlag(command, "I", null, "-", "Less Interactive", "prompt once before removing more than three files, or when removing recursively. Less intrusive than -i, while still giving protection against most mistakes", 1);
        m.addFlag(command, "no-preserve-root", null, "--", "No Preserve Root", "do not treat '/' specially", 1);
        m.addFlag(command, "preserve-root", null, "--", "Preserve Root", "do not remove '/' (default)", 1);
        m.addFlag(command, "r", "-R, --recursive", "--", "Recursive", "remove directories and their contents recursively", 1);
        m.addFlag(command, "v", "--verbose", "--", "Verbose", "explain what is being done", 1);
    }

    private static void createOptions(MainRepository m, Vertex command) {

    }
}
