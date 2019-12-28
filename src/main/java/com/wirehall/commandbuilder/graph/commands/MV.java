package com.wirehall.commandbuilder.graph.commands;

import com.wirehall.commandbuilder.graph.GraphHelper;
import com.wirehall.commandbuilder.graph.SchemaManager;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class MV {
    public static void createData(GraphTraversalSource g) {
        Vertex command = GraphHelper.addCommand(g, "mv", "Move (Rename) Files", null);
        createFlags(g, command);
        createOptions(g, command);
    }

    private static void createFlags(GraphTraversalSource g, Vertex command) {
        GraphHelper.addFlag(g, command, "b", null, "-", "Backup", "make a backup of each existing destination file", 1);
        GraphHelper.addFlag(g, command, "f", "--force", "-", "Force", "do not prompt before overwriting", 1);
        GraphHelper.addFlag(g, command, "i", "--interactive", "-", "Interactive", "prompt before overwrite", 1);
        GraphHelper.addFlag(g, command, "n", "--no-clobber", "-", "No Clobber", "do not overwrite an existing file", 1);
        GraphHelper.addFlag(g, command, "n", "--no-clobber", "-", "No Clobber", "do not overwrite an existing file", 1);
        GraphHelper.addFlag(g, command, "strip-trailing-slashes", null, "--", "Strip Trailing Slashes", "remove any trailing slashes from each SOURCE argument", 1);
        GraphHelper.addFlag(g, command, "T", "--no-target-directory", "-", "No Target Directory", "treat DEST as a normal file", 1);
        GraphHelper.addFlag(g, command, "u", "--update", "-", "Update", "move only when the SOURCE file is newer than the destination file or when the destination file is missing", 1);
        GraphHelper.addFlag(g, command, "v", "--verbose", "-", "Verbose", "explain what is being done", 1);
    }

    private static void createOptions(GraphTraversalSource g, Vertex command) {
        GraphHelper.addOption(g, command, "<SOURCE>", null, null, "Source", null, SchemaManager.TYPE.PATH, true, true, 10);
        GraphHelper.addOption(g, command, "<DEST>", null, null, "Destination", null, SchemaManager.TYPE.PATH, true, true, 10);
    }
}
