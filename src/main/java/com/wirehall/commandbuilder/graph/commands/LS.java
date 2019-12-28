package com.wirehall.commandbuilder.graph.commands;

import com.wirehall.commandbuilder.graph.GraphHelper;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class LS {
    public static void createData(GraphTraversalSource g) {
        Vertex command = GraphHelper.addCommand(g, "ls", "List directory contents", "List information about the FILEs (the current directory by default). Sort entries alphabetically if none of -cftuvSUX nor --sort is specified. Mandatory arguments to long options are mandatory for short options too.");
        createFlags(g, command);
        createOptions(g, command);
    }

    private static void createFlags(GraphTraversalSource g, Vertex command) {
        GraphHelper.addFlag(g, command, "a", "--all", "-", "All", "do not ignore entries starting with .", 1);
        GraphHelper.addFlag(g, command, "A", "--almost-all", "-", "Do not list implied . and ..", null, 1);
        GraphHelper.addFlag(g, command, "B", "--ignore-backups", "-", "Do not list implied entries ending with ~", null, 1);
        GraphHelper.addFlag(g, command, "C", null, "-", "List entries by columns", null, 1);
        GraphHelper.addFlag(g, command, "d", "--directory", "-", "Directory", "List directory entries instead of contents, and do not dereference symbolic links", 1);
        GraphHelper.addFlag(g, command, "G", "--no-group", "-", "No Group", "In a long listing, don't print group names", 1);
        GraphHelper.addFlag(g, command, "h", "--human-readable", "-", "Human Readable Size", "With -l, print sizes in human readable format (e.g., 1K 234M 2G)", 1);
        GraphHelper.addFlag(g, command, "H", "--dereference-command-line", "-", "Dereference Command Line", "Follow symbolic links listed on the command line", 1);
        GraphHelper.addFlag(g, command, "dereference-command-line-symlink-to-dir", null, "--", "Follow Symlink to Dir", "Follow each command line symbolic link that points to a directory", 1);
        GraphHelper.addFlag(g, command, "i", "--inode", "-", "Print the index number of each file", null, 1);
        GraphHelper.addFlag(g, command, "k", null, "-", "Like --block-size=1K", null, 1);
        GraphHelper.addFlag(g, command, "l", null, "-", "Use a long listing format", null, 1);
        GraphHelper.addFlag(g, command, "L", "--dereference", "-", "Dereference","When showing file information for a symbolic link, show information for the file the link references rather than for the link itself", 1);
        GraphHelper.addFlag(g, command, "m", null, "-", "Fill width with a comma separated list of entries",null, 1);
        GraphHelper.addFlag(g, command, "o", null, "-", "Like -l, but do not list group information",null, 1);
        GraphHelper.addFlag(g, command, "Q", "--quote-name", "-", "Enclose entry names in double quotes",null, 1);
        GraphHelper.addFlag(g, command, "r", "--reverse", "-", "Reverse","Reverse order while sorting", 1);
        GraphHelper.addFlag(g, command, "R", "--recursive", "-", "Recursive","List subdirectories recursively", 1);
        GraphHelper.addFlag(g, command, "s", "--size", "-", "Print the allocated size of each file, in blocks",null, 1);
        GraphHelper.addFlag(g, command, "S", null, "-", "Sort by file size",null, 1);
        GraphHelper.addFlag(g, command, "t", null, "-", "Sort by modification time",null, 1);
        GraphHelper.addFlag(g, command, "U", null, "-", "Do not sort; list entries in directory order",null, 1);
        GraphHelper.addFlag(g, command, "v", null, "-", "Natural sort of (version) numbers within text",null, 1);
        GraphHelper.addFlag(g, command, "x", null, "-", "List entries by lines instead of by columns",null, 1);
        GraphHelper.addFlag(g, command, "X", null, "-", "Sort alphabetically by entry extension",null, 1);

    }

    private static void createOptions(GraphTraversalSource g, Vertex command) {

    }
}
