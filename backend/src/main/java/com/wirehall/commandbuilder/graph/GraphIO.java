package com.wirehall.commandbuilder.graph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "app.graph.export.enable", matchIfMissing = true)
public class GraphIO {

  private static final Logger LOGGER = LoggerFactory.getLogger(GraphIO.class);

  private final JanusGraph janusGraph;
  private final GraphTraversalSource gt;

  @Value("${app.graph.export.path}")
  private String exportPath;

  @Value("${app.graph.import.path}")
  private String importPath;

  @Autowired
  public GraphIO(JanusGraph janusGraph, GraphTraversalSource gt) {
    this.janusGraph = janusGraph;
    this.gt = gt;
  }


  /**
   * Export the database in graphml format.
   */
  @Scheduled(cron = "${app.graph.export.cronExpression}")

  public void exportGraphMl() throws IOException {
    Files.createDirectories(Paths.get(exportPath));
    String outputFilePath = exportPath + File.separator + System.currentTimeMillis() + ".graphml";
    try {
      LOGGER.info("Exporting graph to file {}", exportPath);
      gt.io(outputFilePath).with(IO.writer, IO.graphml).write().iterate();
      janusGraph.tx().commit();
      LOGGER.info("Graph export successful!");
    } catch (Exception e) {
      janusGraph.tx().rollback();
      LOGGER.info("Failed to export graph", e);
    }
  }

  /**
   * Import the database from graphml file.
   */
  public void importGraphMl() {
    try {
      LOGGER.info("Importing graph from file {}", importPath);
      gt.io(importPath).with(IO.reader, IO.graphml).read().iterate();
      janusGraph.tx().commit();
      LOGGER.info("Graph import successful!");
    } catch (Exception e) {
      janusGraph.tx().rollback();
      LOGGER.info("Failed to import graph", e);
    }
  }
}
