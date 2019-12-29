package com.wirehall.commandbuilder.graph.commands;

import com.wirehall.commandbuilder.graph.SchemaManager;
import com.wirehall.commandbuilder.repository.MainRepository;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class CP {
    public static void createData(MainRepository m) {
        Vertex command = m.addCommand("cp", "Copy Files and Directories", "Copy SOURCE to DEST, or multiple SOURCE(s) to DIRECTORY");
        createFlags(m, command);
        createOptions(m, command);
    }

    private static void createFlags(MainRepository m, Vertex command) {
        m.addFlag(command, "a", "--archive", "-", "Archive", "When performing the copy, attempt to preserve as much of the original file structure, attributes, and associated metadata as possible.", 1);
        m.addFlag(command, "b", "--backup", "-", "Backup", "Make a backup of each existing destination file", 2);

        m.addFlag(command, "f", "--force", "-", "Force", "if an existing destination file cannot be opened, remove it and try again (redundant if the -n option is used)", 2);
        m.addFlag(command, "i", "--interactive", "-", "Interactive", "Prompt before overwrite (overrides a previous -n option)", 2);
        m.addFlag(command, "H", null, "-", "Follow command-line symbolic links in SOURCE", null, 2);
        m.addFlag(command, "l", "--link", "-", "Link", "Link files instead of copying", 2);
        m.addFlag(command, "L", "--dereference", "-", "Dereference", "Always follow symbolic links in SOURCE", 2);
        m.addFlag(command, "n", "--no-clobber", "-", "No Clobber", "Do not overwrite an existing file(overrides a previous -i option)", 2);
        m.addFlag(command, "P", "--no-dereference", "-", "No Dereference", "Never follow symbolic links in SOURCE", 2);
        m.addFlag(command, "r", "-R, --recursive", "-", "Recursive", "Copy directories recursively", 2);
        m.addFlag(command, "s", "--symbolic-link", "-", "Symbolic Link", "Make symbolic links instead of copying", 2);
        m.addFlag(command, "T", "--no-target-directory", "-", "Treat DEST as a normal file", null, 2);
        m.addFlag(command, "u", "--update", "-", "Update", "Copy only when the SOURCE file is newer than the destination file or when the destination file is missing", 2);
        m.addFlag(command, "v", "--verbose", "-", "Verbose", "Explain what is being done", 2);
    }

    private static void createOptions(MainRepository m, Vertex command) {
        m.addOption(command, "<SOURCE>", null, null, "Source", null, SchemaManager.TYPE.PATH, true, true, 10);
        m.addOption(command, "<DEST>", null, null, "Destination", null, SchemaManager.TYPE.PATH, true, true, 10);
    }
}
