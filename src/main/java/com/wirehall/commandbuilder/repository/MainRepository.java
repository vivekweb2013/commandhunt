package com.wirehall.commandbuilder.repository;

import com.wirehall.commandbuilder.dto.Command;
import com.wirehall.commandbuilder.dto.Filter;
import com.wirehall.commandbuilder.dto.Flag;
import com.wirehall.commandbuilder.dto.Option;
import com.wirehall.commandbuilder.mapper.MainMapper;
import com.wirehall.commandbuilder.model.*;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
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

    public List<Command> getAllCommands() {
        List<Vertex> vertices = g.V().hasLabel(VERTEX.command.toString()).toList();
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

    public void addCommand(Command command) {
        GraphTraversal<Vertex, Vertex> graphTraversal = g.addV(VERTEX.command.toString());

        for (COMMAND_PROPERTY property : COMMAND_PROPERTY.values()) {
            if (command.getProperty(property) != null) {
                graphTraversal.property(property.toString(), command.getProperty(property));
            }
        }
        Vertex commandVertex = graphTraversal.next();

        for (Flag flag : command.getFlags()) {
            addFlag(commandVertex, flag);
        }

        for (Option option : command.getOptions()) {
            addOption(commandVertex, option);
        }
    }

    private void addFlag(Vertex commandVertex, Flag flag) {
        Vertex flagVertex = g.addV(VERTEX.flag.toString()).next();
        GraphTraversal<Vertex, Vertex> vertexGraphTraversal = g.V(flagVertex);
        GraphTraversal<Vertex, Edge> edgeGraphTraversal = g.V(commandVertex).as("a").V(flagVertex).addE(EDGE.has_flag.toString());

        for (FLAG_PROPERTY property : FLAG_PROPERTY.values()) {
            if (flag.getProperty(property) != null && property.propertyOf().equals("V")) {
                vertexGraphTraversal.property(property.toString(), flag.getProperty(property));
            } else if (flag.getProperty(property) != null && property.propertyOf().equals("E")) {
                edgeGraphTraversal.property(property.toString(), flag.getProperty(property));
            }
        }
        vertexGraphTraversal.next();
        edgeGraphTraversal.from("a").next();
    }

    private void addOption(Vertex commandVertex, Option option) {
        Vertex optionVertex = g.addV(VERTEX.option.toString()).next();
        GraphTraversal<Vertex, Vertex> vertexGraphTraversal = g.V(optionVertex);
        GraphTraversal<Vertex, Edge> edgeGraphTraversal = g.V(commandVertex).as("a").V(optionVertex).addE(EDGE.has_option.toString());

        for (OPTION_PROPERTY property : OPTION_PROPERTY.values()) {
            if (option.getProperty(property) != null && property.propertyOf().equals("V")) {
                vertexGraphTraversal.property(property.toString(), option.getProperty(property));
            } else if (option.getProperty(property) != null && property.propertyOf().equals("E")) {
                edgeGraphTraversal.property(property.toString(), option.getProperty(property));
            }
        }
        vertexGraphTraversal.next();
        edgeGraphTraversal.from("a").next();
    }


}
