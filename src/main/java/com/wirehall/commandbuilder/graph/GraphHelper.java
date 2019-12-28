package com.wirehall.commandbuilder.graph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class GraphHelper {

    public static Vertex addCommand(GraphTraversalSource g, String name, String desc, String longDesc) {
        final GraphTraversal<Vertex, Vertex> commandGT = g.addV(SchemaManager.VERTEX.command.toString())
                .property(SchemaManager.PROPERTIES.name.toString(), name)
                .property(SchemaManager.PROPERTIES.desc.toString(), desc);
        if (longDesc != null && !longDesc.isEmpty()) {
            commandGT.property(SchemaManager.PROPERTIES.long_desc.toString(), longDesc);
        }
        return commandGT.next();
    }

    public static void addFlag(GraphTraversalSource g, Vertex command, String name, String alias, String prefix, String desc, String longDesc, int sequence) {
        final GraphTraversal<Vertex, Vertex> flagGT = g.addV(SchemaManager.VERTEX.flag.toString())
                .property(SchemaManager.PROPERTIES.name.toString(), name)
                .property(SchemaManager.PROPERTIES.prefix.toString(), prefix)
                .property(SchemaManager.PROPERTIES.desc.toString(), desc)
                .property(SchemaManager.PROPERTIES.long_desc.toString(), longDesc)
                .property(SchemaManager.PROPERTIES.is_groupable.toString(), true);

        if (alias != null && !alias.isEmpty()) {
            flagGT.property(SchemaManager.PROPERTIES.alias.toString(), alias);
        }
        if (longDesc != null && !longDesc.isEmpty()) {
            flagGT.property(SchemaManager.PROPERTIES.long_desc.toString(), longDesc);
        }

        final Vertex flagVertex = flagGT.next();

        g.V(command).as("a").V(flagVertex).addE(SchemaManager.EDGE.has_flag.toString())
                .property(SchemaManager.PROPERTIES.sequence.toString(), sequence).from("a").next();
    }

    public static void addOption(GraphTraversalSource g, Vertex command, String name, String alias, String prefix, String desc, String longDesc, SchemaManager.TYPE type, boolean isMandatory, boolean isRepeatable, int sequence) {
        final GraphTraversal<Vertex, Vertex> optionGT = g.addV(SchemaManager.VERTEX.option.toString())
                .property(SchemaManager.PROPERTIES.name.toString(), name)
                .property(SchemaManager.PROPERTIES.desc.toString(), desc)
                .property(SchemaManager.PROPERTIES.type.toString(), type.toString());

        if (alias != null && !alias.isEmpty()) {
            optionGT.property(SchemaManager.PROPERTIES.alias.toString(), alias);
        }
        if (prefix != null && !prefix.isEmpty()) {
            optionGT.property(SchemaManager.PROPERTIES.prefix.toString(), prefix);
        }
        if (longDesc != null && !longDesc.isEmpty()) {
            optionGT.property(SchemaManager.PROPERTIES.long_desc.toString(), longDesc);
        }

        final Vertex optionVertex = optionGT.next();
        g.V(command).as("a").V(optionVertex).addE(SchemaManager.EDGE.has_option.toString())
                .property(SchemaManager.PROPERTIES.is_mandatory.toString(), isMandatory)
                .property(SchemaManager.PROPERTIES.is_repeatable.toString(), isRepeatable)
                .property(SchemaManager.PROPERTIES.sequence.toString(), sequence).from("a").next();

//        command.addEdge(SchemaManager.EDGE.has_option.toString(), optionVertex, SchemaManager.PROPERTIES.is_mandatory.toString(), isMandatory, SchemaManager.PROPERTIES.is_repeatable.toString(), isRepeatable, SchemaManager.PROPERTIES.sequence.toString(), sequence);
    }
}
