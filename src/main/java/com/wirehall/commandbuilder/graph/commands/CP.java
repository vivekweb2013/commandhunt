package com.wirehall.commandbuilder.graph.commands;

import com.wirehall.commandbuilder.dto.Command;
import com.wirehall.commandbuilder.model.DATA_TYPE;
import com.wirehall.commandbuilder.repository.MainRepository;
import com.wirehall.commandbuilder.util.NodeBuilder;

public class CP {
    public static void createData(MainRepository repo) {
        Command c = NodeBuilder.buildCommand("cp", "Copy Files and Directories", "Copy SOURCE to DEST, or multiple SOURCE(s) to DIRECTORY");

        NodeBuilder.addFlag(c, "a", "--archive", "-", "Archive", "When performing the copy, attempt to preserve as much of the original file structure, attributes, and associated metadata as possible.", 1);
        NodeBuilder.addFlag(c, "b", "--backup", "-", "Backup", "Make a backup of each existing destination file", 2);

        NodeBuilder.addFlag(c, "f", "--force", "-", "Force", "if an existing destination file cannot be opened, remove it and try again (redundant if the -n option is used)", 2);
        NodeBuilder.addFlag(c, "i", "--interactive", "-", "Interactive", "Prompt before overwrite (overrides a previous -n option)", 2);
        NodeBuilder.addFlag(c, "H", null, "-", "Follow command-line symbolic links in SOURCE", null, 2);
        NodeBuilder.addFlag(c, "l", "--link", "-", "Link", "Link files instead of copying", 2);
        NodeBuilder.addFlag(c, "L", "--dereference", "-", "Dereference", "Always follow symbolic links in SOURCE", 2);
        NodeBuilder.addFlag(c, "n", "--no-clobber", "-", "No Clobber", "Do not overwrite an existing file(overrides a previous -i option)", 2);
        NodeBuilder.addFlag(c, "P", "--no-dereference", "-", "No Dereference", "Never follow symbolic links in SOURCE", 2);
        NodeBuilder.addFlag(c, "r", "-R, --recursive", "-", "Recursive", "Copy directories recursively", 2);
        NodeBuilder.addFlag(c, "s", "--symbolic-link", "-", "Symbolic Link", "Make symbolic links instead of copying", 2);
        NodeBuilder.addFlag(c, "T", "--no-target-directory", "-", "Treat DEST as a normal file", null, 2);
        NodeBuilder.addFlag(c, "u", "--update", "-", "Update", "Copy only when the SOURCE file is newer than the destination file or when the destination file is missing", 2);
        NodeBuilder.addFlag(c, "v", "--verbose", "-", "Verbose", "Explain what is being done", 2);

        NodeBuilder.addOption(c, "<SOURCE>", null, null, "Source", null, DATA_TYPE.PATH, true, true, 10);
        NodeBuilder.addOption(c, "<DEST>", null, null, "Destination", null, DATA_TYPE.PATH, true, true, 10);

        repo.addCommand(c);
    }
}
