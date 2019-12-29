package com.wirehall.commandbuilder.graph.commands;

import com.wirehall.commandbuilder.graph.SchemaManager;
import com.wirehall.commandbuilder.repository.MainRepository;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class PS {
    public static void createData(MainRepository m) {
        Vertex command = m.addCommand("ps", "Report a snapshot of the current processes", "ps displays information about a selection of the active processes. If you want a repetitive update of the selection and the displayed information, use top(1) instead.");
        createFlags(m, command);
        createOptions(m, command);
    }

    private static void createFlags(MainRepository m, Vertex command) {
        m.addFlag(command, "e", "-A", "-", "Backup", "make a backup of each existing destination file", 1);
        m.addFlag(command, "f", null, "-", "Format", "does full-format listing. This option can be combined with many other UNIX-style options to add additional columns. It also causes the command arguments to be printed. When used with -L, the NLWP (number of threads) and LWP (thread ID) columns will be added. See the c option, the format keyword args, and the format keyword comm.", 1);
        m.addFlag(command, "l", null, "-", "Long Format", "long format. The -y option is often useful with this.", 1);
        m.addFlag(command, "H", null, "-", "Process Hierarchy", "show process hierarchy (forest).", 1);

    }

    private static void createOptions(MainRepository m, Vertex command) {
        m.addOption(command, "<USER_LIST>", "--user", "-u", "Filter By Users", "Select by real user ID (RUID) or name. It selects the processes whose real user name or ID is in the userlist list. The real user ID identifies the user who created the process, see getuid(2).", SchemaManager.TYPE.STRING, false, false, 1);
        m.addOption(command, "<PID_LIST>", "--pid", "-p", "Filter By PIDs", "Select by PID.  This selects the processes whose process ID numbers appear in pidlist.  Identical to p and --pid.", SchemaManager.TYPE.STRING, false, false, 1);
        m.addOption(command, "<SESSION_LIST>", "--sid", "-s", "Filter By Sessions", "Select by session ID.  This selects the processes with a session ID specified in sesslist.", SchemaManager.TYPE.STRING, false, false, 1);
    }
}
