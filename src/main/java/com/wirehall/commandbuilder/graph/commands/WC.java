package com.wirehall.commandbuilder.graph.commands;

import com.wirehall.commandbuilder.dto.Command;
import com.wirehall.commandbuilder.model.DATA_TYPE;
import com.wirehall.commandbuilder.repository.MainRepository;
import com.wirehall.commandbuilder.util.NodeBuilder;

public class WC {
    public static void createData(MainRepository repo) {
        Command c = NodeBuilder.buildCommand("wc", "Word Count - print newline, word, and byte counts for each file", "Print newline, word, and byte counts for each FILE, and a total line if more than one FILE is specified. With no FILE, or when FILE is -, read standard input.");

        NodeBuilder.addFlag(c, "c", "--bytes", "-", "print the byte counts", null, 1);
        NodeBuilder.addFlag(c, "m", "--chars", "-", "print the character counts", null, 1);
        NodeBuilder.addFlag(c, "l", "--lines", "-", "print the newline counts", null, 1);
        NodeBuilder.addFlag(c, "L", "--max-line-length", "-", "print the length of the longest line", null, 1);
        NodeBuilder.addFlag(c, "w", "--words", "-", "print the word counts", null, 1);

        NodeBuilder.addOption(c, "<FILE>", null, null, "File Path", null, DATA_TYPE.PATH, true, true, 10);

        repo.addCommand(c);
    }
}
