package com.wirehall.commandbuilder.graph.commands;

import com.wirehall.commandbuilder.dto.Command;
import com.wirehall.commandbuilder.model.DATA_TYPE;
import com.wirehall.commandbuilder.repository.MainRepository;
import com.wirehall.commandbuilder.util.NodeBuilder;

public class MV {
    public static void createData(MainRepository repo) {
        Command c = NodeBuilder.buildCommand("mv", "Move (Rename) Files", null);

        NodeBuilder.addFlag(c, "b", null, "-", "Backup", "make a backup of each existing destination file", 1);
        NodeBuilder.addFlag(c, "f", "--force", "-", "Force", "do not prompt before overwriting", 1);
        NodeBuilder.addFlag(c, "i", "--interactive", "-", "Interactive", "prompt before overwrite", 1);
        NodeBuilder.addFlag(c, "n", "--no-clobber", "-", "No Clobber", "do not overwrite an existing file", 1);
        NodeBuilder.addFlag(c, "n", "--no-clobber", "-", "No Clobber", "do not overwrite an existing file", 1);
        NodeBuilder.addFlag(c, "strip-trailing-slashes", null, "--", "Strip Trailing Slashes", "remove any trailing slashes from each SOURCE argument", 1);
        NodeBuilder.addFlag(c, "T", "--no-target-directory", "-", "No Target Directory", "treat DEST as a normal file", 1);
        NodeBuilder.addFlag(c, "u", "--update", "-", "Update", "move only when the SOURCE file is newer than the destination file or when the destination file is missing", 1);
        NodeBuilder.addFlag(c, "v", "--verbose", "-", "Verbose", "explain what is being done", 1);

        NodeBuilder.addOption(c, "<SOURCE>", null, null, "Source", null, DATA_TYPE.PATH, true, true, 10);
        NodeBuilder.addOption(c, "<DEST>", null, null, "Destination", null, DATA_TYPE.PATH, true, true, 10);

        repo.addCommand(c);
    }
}
