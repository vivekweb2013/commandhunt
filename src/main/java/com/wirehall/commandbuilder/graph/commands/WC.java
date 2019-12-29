package com.wirehall.commandbuilder.graph.commands;

import com.wirehall.commandbuilder.graph.SchemaManager;
import com.wirehall.commandbuilder.repository.MainRepository;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class WC {
    public static void createData(MainRepository m) {
        Vertex command = m.addCommand("wc", "Word Count - print newline, word, and byte counts for each file", "Print newline, word, and byte counts for each FILE, and a total line if more than one FILE is specified. With no FILE, or when FILE is -, read standard input.");
        createFlags(m, command);
        createOptions(m, command);
    }

    private static void createOptions(MainRepository m, Vertex command) {
        m.addFlag(command, "c", "--bytes", "-", "print the byte counts", null, 1);
        m.addFlag(command, "m", "--chars", "-", "print the character counts", null, 1);
        m.addFlag(command, "l", "--lines", "-", "print the newline counts", null, 1);
        m.addFlag(command, "L", "--max-line-length", "-", "print the length of the longest line", null, 1);
        m.addFlag(command, "w", "--words", "-", "print the word counts", null, 1);
    }

    private static void createFlags(MainRepository m, Vertex command) {
        m.addOption(command, "<FILE>", null, null, "File Path", null, SchemaManager.TYPE.PATH, true, true, 10);
    }
}
