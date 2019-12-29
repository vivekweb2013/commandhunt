package com.wirehall.commandbuilder.graph.commands;

import com.wirehall.commandbuilder.repository.MainRepository;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class LS {
    public static void createData(MainRepository m) {
        Vertex command = m.addCommand("ls", "List directory contents", "List information about the FILEs (the current directory by default). Sort entries alphabetically if none of -cftuvSUX nor --sort is specified. Mandatory arguments to long options are mandatory for short options too.");
        createFlags(m, command);
        createOptions(m, command);
    }

    private static void createFlags(MainRepository m, Vertex command) {
        m.addFlag(command, "a", "--all", "-", "All", "do not ignore entries starting with .", 1);
        m.addFlag(command, "A", "--almost-all", "-", "Do not list implied . and ..", null, 1);
        m.addFlag(command, "B", "--ignore-backups", "-", "Do not list implied entries ending with ~", null, 1);
        m.addFlag(command, "C", null, "-", "List entries by columns", null, 1);
        m.addFlag(command, "d", "--directory", "-", "Directory", "List directory entries instead of contents, and do not dereference symbolic links", 1);
        m.addFlag(command, "G", "--no-group", "-", "No Group", "In a long listing, don't print group names", 1);
        m.addFlag(command, "h", "--human-readable", "-", "Human Readable Size", "With -l, print sizes in human readable format (e.g., 1K 234M 2G)", 1);
        m.addFlag(command, "H", "--dereference-command-line", "-", "Dereference Command Line", "Follow symbolic links listed on the command line", 1);
        m.addFlag(command, "dereference-command-line-symlink-to-dir", null, "--", "Follow Symlink to Dir", "Follow each command line symbolic link that points to a directory", 1);
        m.addFlag(command, "i", "--inode", "-", "Print the index number of each file", null, 1);
        m.addFlag(command, "k", null, "-", "Like --block-size=1K", null, 1);
        m.addFlag(command, "l", null, "-", "Use a long listing format", null, 1);
        m.addFlag(command, "L", "--dereference", "-", "Dereference", "When showing file information for a symbolic link, show information for the file the link references rather than for the link itself", 1);
        m.addFlag(command, "m", null, "-", "Fill width with a comma separated list of entries", null, 1);
        m.addFlag(command, "o", null, "-", "Like -l, but do not list group information", null, 1);
        m.addFlag(command, "Q", "--quote-name", "-", "Enclose entry names in double quotes", null, 1);
        m.addFlag(command, "r", "--reverse", "-", "Reverse", "Reverse order while sorting", 1);
        m.addFlag(command, "R", "--recursive", "-", "Recursive", "List subdirectories recursively", 1);
        m.addFlag(command, "s", "--size", "-", "Print the allocated size of each file, in blocks", null, 1);
        m.addFlag(command, "S", null, "-", "Sort by file size", null, 1);
        m.addFlag(command, "t", null, "-", "Sort by modification time", null, 1);
        m.addFlag(command, "U", null, "-", "Do not sort; list entries in directory order", null, 1);
        m.addFlag(command, "v", null, "-", "Natural sort of (version) numbers within text", null, 1);
        m.addFlag(command, "x", null, "-", "List entries by lines instead of by columns", null, 1);
        m.addFlag(command, "X", null, "-", "Sort alphabetically by entry extension", null, 1);

    }

    private static void createOptions(MainRepository m, Vertex command) {

    }
}
