package com.wirehall.commandbuilder.graph.commands;

import com.wirehall.commandbuilder.graph.GraphHelper;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class VI {
    public static void createData(GraphTraversalSource g) {
        Vertex command = GraphHelper.addCommand(g, "vi", "Visual Display Editor", null);
        createFlags(g, command);
        createOptions(g, command);
    }

    private static void createFlags(GraphTraversalSource g, Vertex command) {

    }

    private static void createOptions(GraphTraversalSource g, Vertex command) {

    }
}
