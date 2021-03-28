package com.wirehall.commandhunt.backend.graph;

import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.janusgraph.core.JanusGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class MetadataManager {

  public static final String GRAPH_FILE = "db/db.graphml";
  private static final Logger LOGGER = LoggerFactory.getLogger(MetadataManager.class);
  private final JanusGraph graph;

  /**
   * Performs graph db initialization operations.
   *
   * @param graph Graph instance.
   */
  @Autowired
  public MetadataManager(JanusGraph graph) {
    this.graph = graph;
  }

  /**
   * Import the database from graphml file.
   * <p>
   * This method supports importing graph file from resources path.
   */
  public void load() {
    try {
      SchemaBuilder.load(graph);
      LOGGER.info("Importing graph from file {}", GRAPH_FILE);
      InputStream stream = MetadataManager.class.getClassLoader().getResourceAsStream(GRAPH_FILE);
      graph.io(IoCore.graphml()).reader().create().readGraph(stream, graph);
      graph.tx().commit();
      LOGGER.info("Graph import successful!");
    } catch (Exception e) {
      graph.tx().rollback();
      throw new Error("Failed to import graph", e);
    }
  }
}
