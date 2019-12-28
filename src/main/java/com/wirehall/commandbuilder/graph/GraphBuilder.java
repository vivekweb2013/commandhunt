package com.wirehall.commandbuilder.graph;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.util.GraphFactory;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GraphBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphBuilder.class);

    private Configuration conf;
    private JanusGraph graph;
    private GraphTraversalSource g;

    public GraphBuilder(final String propertyFilePath) throws ConfigurationException {
        conf = new PropertiesConfiguration(propertyFilePath);
        initialize();
    }

    private void initialize() {
        try {
            openGraph();
            SchemaManager.createSchema(graph);
            DataManager.fillData(g);
            readElements();
            closeGraph();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private GraphTraversalSource openGraph() {
        LOGGER.info("opening graph");
        graph = (JanusGraph) GraphFactory.open(conf);
        g = graph.traversal();
        return g;
    }


    private void readElements() {
        try {
            if (g == null) {
                return;
            }

            LOGGER.info("reading elements");

            // look up vertex by name
            final Optional<Map<Object, Object>> v = g.V().has("name", "cp").valueMap(true).tryNext();
            if (v.isPresent()) {
                LOGGER.info(v.get().toString());
            } else {
                LOGGER.warn("cp not found");
            }

            // look up an incident edge
            final Optional<Map<Object, Object>> edge = g.V().has("name", "cp").outE("has_flag").as("e").inV()
                    .has("name", "r").select("e").valueMap(true).tryNext();
            if (edge.isPresent()) {
                LOGGER.info(edge.get().toString());
            } else {
                LOGGER.warn("edge not found");
            }

            // ansible might be deleted
            final boolean plutoExists = g.V().has("name", "ansible").hasNext();
            if (plutoExists) {
                LOGGER.info("ansible exists");
            } else {
                LOGGER.warn("ansible not found");
            }

            // look up options
            final List<Object> options = g.V().has("name", "cp").both("has_option").values("name").dedup().toList();
            LOGGER.info("options: " + options.toString());

        } finally {
            // the default behavior automatically starts a transaction for
            // any graph interaction, so it is best to finish the transaction
            // even for read-only graph query operations
            g.tx().rollback();
        }
    }

    private void closeGraph() throws Exception {
        LOGGER.info("closing graph");
        try {
            if (g != null) {
                g.close();
            }
            if (graph != null) {
                graph.close();
            }
        } finally {
            g = null;
            graph = null;
        }
    }

    public void dropGraph() throws Exception {
        if (graph != null) {
            JanusGraphFactory.drop(graph);
        }
    }


}
