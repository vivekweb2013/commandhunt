package com.wirehall.commandbuilder.graph.commands;

import com.wirehall.commandbuilder.dto.Command;
import com.wirehall.commandbuilder.repository.MainRepository;
import com.wirehall.commandbuilder.util.NodeBuilder;

public class LS {
    public static void createData(MainRepository repo) {
        Command c = NodeBuilder.buildCommand("ls", "List directory contents", "List information about the FILEs (the current directory by default). Sort entries alphabetically if none of -cftuvSUX nor --sort is specified. Mandatory arguments to long options are mandatory for short options too.");

        NodeBuilder.addFlag(c, "a", "--all", "-", "All", "do not ignore entries starting with .", 1);
        NodeBuilder.addFlag(c, "A", "--almost-all", "-", "Do not list implied . and ..", null, 1);
        NodeBuilder.addFlag(c, "B", "--ignore-backups", "-", "Do not list implied entries ending with ~", null, 1);
        NodeBuilder.addFlag(c, "C", null, "-", "List entries by columns", null, 1);
        NodeBuilder.addFlag(c, "d", "--directory", "-", "Directory", "List directory entries instead of contents, and do not dereference symbolic links", 1);
        NodeBuilder.addFlag(c, "G", "--no-group", "-", "No Group", "In a long listing, don't print group names", 1);
        NodeBuilder.addFlag(c, "h", "--human-readable", "-", "Human Readable Size", "With -l, print sizes in human readable format (e.g., 1K 234M 2G)", 1);
        NodeBuilder.addFlag(c, "H", "--dereference-command-line", "-", "Dereference Command Line", "Follow symbolic links listed on the command line", 1);
        NodeBuilder.addFlag(c, "dereference-command-line-symlink-to-dir", null, "--", "Follow Symlink to Dir", "Follow each command line symbolic link that points to a directory", 1);
        NodeBuilder.addFlag(c, "i", "--inode", "-", "Print the index number of each file", null, 1);
        NodeBuilder.addFlag(c, "k", null, "-", "Like --block-size=1K", null, 1);
        NodeBuilder.addFlag(c, "l", null, "-", "Use a long listing format", null, 1);
        NodeBuilder.addFlag(c, "L", "--dereference", "-", "Dereference", "When showing file information for a symbolic link, show information for the file the link references rather than for the link itself", 1);
        NodeBuilder.addFlag(c, "m", null, "-", "Fill width with a comma separated list of entries", null, 1);
        NodeBuilder.addFlag(c, "o", null, "-", "Like -l, but do not list group information", null, 1);
        NodeBuilder.addFlag(c, "Q", "--quote-name", "-", "Enclose entry names in double quotes", null, 1);
        NodeBuilder.addFlag(c, "r", "--reverse", "-", "Reverse", "Reverse order while sorting", 1);
        NodeBuilder.addFlag(c, "R", "--recursive", "-", "Recursive", "List subdirectories recursively", 1);
        NodeBuilder.addFlag(c, "s", "--size", "-", "Print the allocated size of each file, in blocks", null, 1);
        NodeBuilder.addFlag(c, "S", null, "-", "Sort by file size", null, 1);
        NodeBuilder.addFlag(c, "t", null, "-", "Sort by modification time", null, 1);
        NodeBuilder.addFlag(c, "U", null, "-", "Do not sort; list entries in directory order", null, 1);
        NodeBuilder.addFlag(c, "v", null, "-", "Natural sort of (version) numbers within text", null, 1);
        NodeBuilder.addFlag(c, "x", null, "-", "List entries by lines instead of by columns", null, 1);
        NodeBuilder.addFlag(c, "X", null, "-", "Sort alphabetically by entry extension", null, 1);

        repo.addCommand(c);
    }
}
