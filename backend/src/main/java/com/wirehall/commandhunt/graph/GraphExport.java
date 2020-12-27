package com.wirehall.commandhunt.graph;

import org.apache.tinkerpop.gremlin.process.traversal.IO;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.JanusGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "app.graph.export.enable", matchIfMissing = true)
public class GraphExport {
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphExport.class);

    @Value("${app.graph.export.path}")
    private String exportPath;

    private final JanusGraph janusGraph;
    private final GraphTraversalSource gt;

    @Autowired
    public GraphExport(JanusGraph janusGraph, GraphTraversalSource gt) {
        this.janusGraph = janusGraph;
        this.gt = gt;
    }

    @Scheduled(cron = "${app.graph.export.cronExpression}")
    public void exportGraphMl() throws IOException {
        Files.createDirectories(Paths.get(exportPath));
        String outputFilePath = exportPath + File.separator + System.currentTimeMillis() + ".graphml";
        try {
            LOGGER.info("Exporting graph...");
            gt.io(outputFilePath).with(IO.writer, IO.graphml).write().iterate();
            janusGraph.tx().commit();
            LOGGER.info("Graph exported!");
        } catch (Exception e) {
            janusGraph.tx().rollback();
            LOGGER.info("Failed to export graph", e);
        }
    }
}
