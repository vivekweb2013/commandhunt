package com.wirehall.commandbuilder.graph.commands;

import com.wirehall.commandbuilder.graph.GraphHelper;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class RM {
    public static void createData(GraphTraversalSource g) {
        Vertex command = GraphHelper.addCommand(g, "rm", "Remove Files or Directories", "rm removes each specified file. By default, it does not remove directories.");
        createFlags(g, command);
        createOptions(g, command);
    }

    private static void createFlags(GraphTraversalSource g, Vertex command) {
        GraphHelper.addFlag(g, command, "f", "--force", "-", "Force", "ignore nonexistent files, never prompt", 1);
        GraphHelper.addFlag(g, command, "i", null, "-", "Interactive", "prompt before every removal", 1);
        GraphHelper.addFlag(g, command, "I", null, "-", "Less Interactive", "prompt once before removing more than three files, or when removing recursively. Less intrusive than -i, while still giving protection against most mistakes", 1);
        GraphHelper.addFlag(g, command, "no-preserve-root", null, "--", "No Preserve Root", "do not treat '/' specially", 1);
        GraphHelper.addFlag(g, command, "preserve-root", null, "--", "Preserve Root", "do not remove '/' (default)", 1);
        GraphHelper.addFlag(g, command, "r", "-R, --recursive", "--", "Recursive", "remove directories and their contents recursively", 1);
        GraphHelper.addFlag(g, command, "v", "--verbose", "--", "Verbose", "explain what is being done", 1);
    }

    private static void createOptions(GraphTraversalSource g, Vertex command) {

    }
}
