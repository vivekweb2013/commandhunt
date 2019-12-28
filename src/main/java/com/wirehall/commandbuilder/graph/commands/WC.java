package com.wirehall.commandbuilder.graph.commands;

import com.wirehall.commandbuilder.graph.GraphHelper;
import com.wirehall.commandbuilder.graph.SchemaManager;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class WC {
    public static void createData(GraphTraversalSource g) {
        Vertex command = GraphHelper.addCommand(g, "wc", "Word Count - print newline, word, and byte counts for each file", "Print newline, word, and byte counts for each FILE, and a total line if more than one FILE is specified. With no FILE, or when FILE is -, read standard input.");
        createFlags(g, command);
        createOptions(g, command);
    }

    private static void createOptions(GraphTraversalSource g, Vertex command) {
        GraphHelper.addFlag(g, command, "c", "--bytes", "-", "print the byte counts", null, 1);
        GraphHelper.addFlag(g, command, "m", "--chars", "-", "print the character counts", null, 1);
        GraphHelper.addFlag(g, command, "l", "--lines", "-", "print the newline counts", null, 1);
        GraphHelper.addFlag(g, command, "L", "--max-line-length", "-", "print the length of the longest line", null, 1);
        GraphHelper.addFlag(g, command, "w", "--words", "-", "print the word counts", null, 1);
    }

    private static void createFlags(GraphTraversalSource g, Vertex command) {
        GraphHelper.addOption(g, command, "<FILE>", null, null, "File Path", null, SchemaManager.TYPE.PATH, true, true, 10);
    }
}
