package com.wirehall.commandbuilder.graph;

import com.wirehall.commandbuilder.graph.commands.*;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataManager.class);

    public static void fillData(GraphTraversalSource g) {
        try {
            // naive check if the graph was previously created
            if (g.V().has("name", "command").hasNext()) {
                g.tx().rollback();
                return;
            }
            LOGGER.info("creating elements");

            CP.createData(g);
            LS.createData(g);
            MV.createData(g);
            PS.createData(g);
            RM.createData(g);
            VI.createData(g);
            WC.createData(g);

            g.tx().commit();

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            g.tx().rollback();
        }
    }

}
