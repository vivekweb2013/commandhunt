package com.wirehall.commandhunt.initdb;

import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.janusgraph.core.JanusGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class MetadataManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataManager.class);
    public static final String GRAPH_FILE = "db.graphml";

    private MetadataManager() {
        // Utility classes should not have public constructors
    }

    /**
     * Import the database from graphml file.
     *
     * @param graph The janus graph instance.
     */
    public static void load(final JanusGraph graph) {
        try {
            LOGGER.info("Importing graph from file {}", GRAPH_FILE);
            InputStream stream = MetadataManager.class.getClassLoader().getResourceAsStream(GRAPH_FILE);
            graph.io(IoCore.graphml()).reader().create().readGraph(stream, graph);
            graph.tx().commit();
            LOGGER.info("Graph import successful!");
        } catch (Exception e) {
            graph.tx().rollback();
            LOGGER.info("Failed to import graph", e);
            throw new Error(e.getMessage());
        }
    }
}
