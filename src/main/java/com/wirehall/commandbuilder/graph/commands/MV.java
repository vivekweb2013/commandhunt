package com.wirehall.commandbuilder.graph.commands;

import com.wirehall.commandbuilder.graph.SchemaManager;
import com.wirehall.commandbuilder.repository.MainRepository;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class MV {
    public static void createData(MainRepository m) {
        Vertex command = m.addCommand("mv", "Move (Rename) Files", null);
        createFlags(m, command);
        createOptions(m, command);
    }

    private static void createFlags(MainRepository m, Vertex command) {
        m.addFlag(command, "b", null, "-", "Backup", "make a backup of each existing destination file", 1);
        m.addFlag(command, "f", "--force", "-", "Force", "do not prompt before overwriting", 1);
        m.addFlag(command, "i", "--interactive", "-", "Interactive", "prompt before overwrite", 1);
        m.addFlag(command, "n", "--no-clobber", "-", "No Clobber", "do not overwrite an existing file", 1);
        m.addFlag(command, "n", "--no-clobber", "-", "No Clobber", "do not overwrite an existing file", 1);
        m.addFlag(command, "strip-trailing-slashes", null, "--", "Strip Trailing Slashes", "remove any trailing slashes from each SOURCE argument", 1);
        m.addFlag(command, "T", "--no-target-directory", "-", "No Target Directory", "treat DEST as a normal file", 1);
        m.addFlag(command, "u", "--update", "-", "Update", "move only when the SOURCE file is newer than the destination file or when the destination file is missing", 1);
        m.addFlag(command, "v", "--verbose", "-", "Verbose", "explain what is being done", 1);
    }

    private static void createOptions(MainRepository m, Vertex command) {
        m.addOption(command, "<SOURCE>", null, null, "Source", null, SchemaManager.TYPE.PATH, true, true, 10);
        m.addOption(command, "<DEST>", null, null, "Destination", null, SchemaManager.TYPE.PATH, true, true, 10);
    }
}
