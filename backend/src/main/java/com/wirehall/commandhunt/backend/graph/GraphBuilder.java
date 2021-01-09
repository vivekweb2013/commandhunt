package com.wirehall.commandhunt.backend.graph;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wirehall.commandhunt.backend.dto.Command;
import com.wirehall.commandhunt.backend.model.EdgeType;
import com.wirehall.commandhunt.backend.model.VertexType;
import com.wirehall.commandhunt.backend.repository.CommandRepository;
import com.wirehall.commandhunt.initdb.MetadataManager;
import com.wirehall.commandhunt.initdb.SchemaBuilder;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.diskstorage.BackendException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class GraphBuilder {

  private static final Logger LOGGER = LoggerFactory.getLogger(GraphBuilder.class);
  private final CommandRepository commandRepository;
  @Autowired
  private GraphIO graphIO;
  @Value("${app.graph.import.enable}")
  private boolean importEnable;
  @Value("classpath*:/data/**/*.json")
  private Resource[] jsonDataResources;
  private JanusGraph graph;
  private GraphTraversalSource gt;

  /**
   * Performs graph db initialization operations.
   *
   * @param graph             Graph instance.
   * @param gt                Graph Traversal instance.
   * @param commandRepository The command repository.
   */
  @Autowired
  public GraphBuilder(JanusGraph graph, GraphTraversalSource gt,
      CommandRepository commandRepository) {
    this.graph = graph;
    this.gt = gt;
    this.commandRepository = commandRepository;
  }

  /**
   * Creates the schema and imports the data.
   */
  public void initialize() throws InterruptedException {
    SchemaBuilder.load(graph);
    if (importEnable) {
      graphIO.importGraphMl();
    } else {
      MetadataManager.load(graph);
    }
    readElements();
  }

  private void fillData(CommandRepository commandRepository) {
    try {
      // naive check if the graph was previously created
      if (gt.V().hasLabel(VertexType.COMMAND.toLowerCase()).outE(EdgeType.HAS_FLAG.toLowerCase()).hasNext()) {
        LOGGER.info("Skip Data Creation. Data Already Exist!!!");
        gt.tx().rollback();
        return;
      }
      LOGGER.info("Creating Data");

      for (Resource resource : jsonDataResources) {
        ObjectMapper mapper = new ObjectMapper();
        Command command = mapper.readValue(resource.getInputStream(), Command.class);
        commandRepository.addCommand(command);
      }

      gt.tx().commit();

    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      gt.tx().rollback();
    }
  }

  private void readElements() {
    if (gt == null) {
      return;
    }
    try {
      LOGGER.info("reading elements");

      // look up vertex by name
      final Optional<Map<Object, Object>> v = gt.V().hasLabel(VertexType.COMMAND.toLowerCase())
              .has("name", "cp").valueMap().with(WithOptions.tokens).tryNext();

      if (v.isPresent()) {
        LOGGER.info("Vertex: {}", v.get());
      } else {
        LOGGER.warn("cp not found");
      }

      // look up an incident edge
      final Optional<Map<Object, Object>> edge = gt.V()
              .has("name", "cp")
              .outE("has_flag")
              .as("e")
              .inV()
              .has("name", "r")
              .select("e")
              .valueMap()
              .with(WithOptions.tokens)
              .tryNext();
      if (edge.isPresent()) {
        LOGGER.info("Edge {}", edge.get());
      } else {
        LOGGER.warn("Edge not found");
      }

      // ansible might be deleted
      final boolean plutoExists = gt.V().has("name", "ansible").hasNext();
      if (plutoExists) {
        LOGGER.info("ansible exists");
      } else {
        LOGGER.warn("ansible not found");
      }

      // look up options
      final List<Object> options =
          gt.V().has("name", "cp").both("has_option").values("name").dedup().toList();
      LOGGER.info("options: {}", options);

    } finally {
      // the default behavior automatically starts a transaction for
      // any graph interaction, so it is best to finish the transaction
      // even for read-only graph query operations
      gt.tx().rollback();
    }
  }

  private void closeGraph() throws Exception {
    LOGGER.info("closing graph");
    try {
      if (gt != null) {
        gt.close();
      }
      if (graph != null) {
        graph.close();
      }
    } finally {
      gt = null;
      graph = null;
    }
  }

  /**
   * Drops the graph.
   *
   * @throws BackendException Exception occurred while dropping the graph.
   */
  public void dropGraph() throws BackendException {
    if (graph != null) {
      JanusGraphFactory.drop(graph);
    }
  }
}
