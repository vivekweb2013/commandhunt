package com.wirehall.commandbuilder.graph;

import com.wirehall.commandbuilder.graph.commands.*;
import com.wirehall.commandbuilder.repository.MainRepository;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class GraphBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphBuilder.class);

    private JanusGraph graph;
    private GraphTraversalSource g;
    private MainRepository mainRepository;

    @Autowired
    public GraphBuilder(JanusGraph graph, GraphTraversalSource g, MainRepository mainRepository) {
        this.graph = graph;
        this.g = g;
        this.mainRepository = mainRepository;
    }

    public void initialize() {
        try {
            SchemaManager.createSchema(graph);
            fillData(mainRepository);
            readElements();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void fillData(MainRepository mainRepository) {
        try {
            // naive check if the graph was previously created
            if (g.V().has("name", "command").hasNext()) {
                LOGGER.info("Skip Data Creation. Data Already Exist!!!");
                g.tx().rollback();
                return;
            }
            LOGGER.info("Creating Data");

            CP.createData(mainRepository);
            LS.createData(mainRepository);
            MV.createData(mainRepository);
            PS.createData(mainRepository);
            RM.createData(mainRepository);
            VI.createData(mainRepository);
            WC.createData(mainRepository);

            g.tx().commit();

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            g.tx().rollback();
        }
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
