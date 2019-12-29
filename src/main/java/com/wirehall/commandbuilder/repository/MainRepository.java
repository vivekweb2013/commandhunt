package com.wirehall.commandbuilder.repository;

import com.wirehall.commandbuilder.dto.Command;
import com.wirehall.commandbuilder.dto.Filter;
import com.wirehall.commandbuilder.graph.SchemaManager;
import com.wirehall.commandbuilder.mapper.MainMapper;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MainRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainRepository.class);

    private MainMapper mapper = new MainMapper();
    private JanusGraph graph;
    private GraphTraversalSource g;

    @Autowired
    public MainRepository(JanusGraph graph, GraphTraversalSource g) {
        this.graph = graph;
        this.g = g;
    }

    public Vertex addCommand(String name, String desc, String longDesc) {
        final GraphTraversal<Vertex, Vertex> graphTraversal = g.addV(SchemaManager.VERTEX.command.toString())
                .property(SchemaManager.PROPERTIES.name.toString(), name)
                .property(SchemaManager.PROPERTIES.desc.toString(), desc);
        if (longDesc != null && !longDesc.isEmpty()) {
            graphTraversal.property(SchemaManager.PROPERTIES.long_desc.toString(), longDesc);
        }
        return graphTraversal.next();
    }

    public void addOption(Vertex command, String name, String alias, String prefix, String desc, String longDesc,
                          SchemaManager.TYPE type, boolean isMandatory, boolean isRepeatable, int sequence) {
        final GraphTraversal<Vertex, Vertex> graphTraversal = g.addV(SchemaManager.VERTEX.option.toString())
                .property(SchemaManager.PROPERTIES.name.toString(), name)
                .property(SchemaManager.PROPERTIES.desc.toString(), desc)
                .property(SchemaManager.PROPERTIES.type.toString(), type.toString());

        if (alias != null && !alias.isEmpty()) {
            graphTraversal.property(SchemaManager.PROPERTIES.alias.toString(), alias);
        }
        if (prefix != null && !prefix.isEmpty()) {
            graphTraversal.property(SchemaManager.PROPERTIES.prefix.toString(), prefix);
        }
        if (longDesc != null && !longDesc.isEmpty()) {
            graphTraversal.property(SchemaManager.PROPERTIES.long_desc.toString(), longDesc);
        }

        final Vertex optionVertex = graphTraversal.next();
        g.V(command).as("a").V(optionVertex).addE(SchemaManager.EDGE.has_option.toString())
                .property(SchemaManager.PROPERTIES.is_mandatory.toString(), isMandatory)
                .property(SchemaManager.PROPERTIES.is_repeatable.toString(), isRepeatable)
                .property(SchemaManager.PROPERTIES.sequence.toString(), sequence).from("a").next();

        // command.addEdge(SchemaManager.EDGE.has_option.toString(), optionVertex, SchemaManager.PROPERTIES.is_mandatory.toString(), isMandatory, SchemaManager.PROPERTIES.is_repeatable.toString(), isRepeatable, SchemaManager.PROPERTIES.sequence.toString(), sequence);
    }

    public void addFlag(Vertex command, String name, String alias, String prefix, String desc, String longDesc, int sequence) {
        final GraphTraversal<Vertex, Vertex> graphTraversal = g.addV(SchemaManager.VERTEX.flag.toString())
                .property(SchemaManager.PROPERTIES.name.toString(), name)
                .property(SchemaManager.PROPERTIES.prefix.toString(), prefix)
                .property(SchemaManager.PROPERTIES.desc.toString(), desc)
                .property(SchemaManager.PROPERTIES.long_desc.toString(), longDesc)
                .property(SchemaManager.PROPERTIES.is_groupable.toString(), true);

        if (alias != null && !alias.isEmpty()) {
            graphTraversal.property(SchemaManager.PROPERTIES.alias.toString(), alias);
        }
        if (longDesc != null && !longDesc.isEmpty()) {
            graphTraversal.property(SchemaManager.PROPERTIES.long_desc.toString(), longDesc);
        }

        final Vertex flagVertex = graphTraversal.next();

        g.V(command).as("a").V(flagVertex).addE(SchemaManager.EDGE.has_flag.toString())
                .property(SchemaManager.PROPERTIES.sequence.toString(), sequence).from("a").next();
    }

    public List<Command> getAllCommands() {
        List<Vertex> vertices = g.V().hasLabel(SchemaManager.VERTEX.command.toString()).toList();
        return mapper.mapToCommands(vertices);
    }

    public Command getCommandById(String id) {
        Command command = new Command();
        return command;
    }

    public Command getCommandByName(String name) {
        Command command = new Command();
        return command;
    }

    public List<Command> getCommandsByFilter(Filter filter) {
        List<Command> commands = new ArrayList<>();
        return commands;
    }
}
