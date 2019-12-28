package com.wirehall.commandbuilder.graph.commands;

import com.wirehall.commandbuilder.graph.GraphHelper;
import com.wirehall.commandbuilder.graph.SchemaManager;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class CP {
    public static void createData(GraphTraversalSource g) {
        Vertex command = GraphHelper.addCommand(g, "cp", "Copy Files and Directories", "Copy SOURCE to DEST, or multiple SOURCE(s) to DIRECTORY");
        createFlags(g, command);
        createOptions(g, command);
    }

    private static void createFlags(GraphTraversalSource g, Vertex command) {
        GraphHelper.addFlag(g, command, "a", "--archive", "-", "Archive", "When performing the copy, attempt to preserve as much of the original file structure, attributes, and associated metadata as possible.", 1);
        GraphHelper.addFlag(g, command, "b", "--backup", "-", "Backup", "Make a backup of each existing destination file", 2);

        GraphHelper.addFlag(g, command, "f", "--force", "-", "Force", "if an existing destination file cannot be opened, remove it and try again (redundant if the -n option is used)", 2);
        GraphHelper.addFlag(g, command, "i", "--interactive", "-", "Interactive", "Prompt before overwrite (overrides a previous -n option)", 2);
        GraphHelper.addFlag(g, command, "H", null, "-", "Follow command-line symbolic links in SOURCE", null, 2);
        GraphHelper.addFlag(g, command, "l", "--link", "-", "Link", "Link files instead of copying", 2);
        GraphHelper.addFlag(g, command, "L", "--dereference", "-", "Dereference", "Always follow symbolic links in SOURCE", 2);
        GraphHelper.addFlag(g, command, "n", "--no-clobber", "-", "No Clobber", "Do not overwrite an existing file(overrides a previous -i option)", 2);
        GraphHelper.addFlag(g, command, "P", "--no-dereference", "-", "No Dereference", "Never follow symbolic links in SOURCE", 2);
        GraphHelper.addFlag(g, command, "r", "-R, --recursive", "-", "Recursive", "Copy directories recursively", 2);
        GraphHelper.addFlag(g, command, "s", "--symbolic-link", "-", "Symbolic Link", "Make symbolic links instead of copying", 2);
        GraphHelper.addFlag(g, command, "T", "--no-target-directory", "-", "Treat DEST as a normal file", null, 2);
        GraphHelper.addFlag(g, command, "u", "--update", "-", "Update", "Copy only when the SOURCE file is newer than the destination file or when the destination file is missing", 2);
        GraphHelper.addFlag(g, command, "v", "--verbose", "-", "Verbose", "Explain what is being done", 2);
    }

    private static void createOptions(GraphTraversalSource g, Vertex command) {
        GraphHelper.addOption(g, command, "<SOURCE>", null, null, "Source", null, SchemaManager.TYPE.PATH, true, true, 10);
        GraphHelper.addOption(g, command, "<DEST>", null, null, "Destination", null, SchemaManager.TYPE.PATH, true, true, 10);
    }
}
