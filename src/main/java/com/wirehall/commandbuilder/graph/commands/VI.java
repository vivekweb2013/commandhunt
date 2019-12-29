package com.wirehall.commandbuilder.graph.commands;

import com.wirehall.commandbuilder.repository.MainRepository;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class VI {
    public static void createData(MainRepository m) {
        Vertex command = m.addCommand("vi", "Visual Display Editor", null);
        createFlags(m, command);
        createOptions(m, command);
    }

    private static void createFlags(MainRepository m, Vertex command) {

    }

    private static void createOptions(MainRepository m, Vertex command) {

    }
}
