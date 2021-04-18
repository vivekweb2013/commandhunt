package com.wirehall.commandhunt.backend.graph;

import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.Cardinality;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.Multiplicity;
import org.janusgraph.core.RelationType;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.graphdb.database.management.ManagementSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchemaBuilder {
  private static final Logger LOGGER = LoggerFactory.getLogger(SchemaBuilder.class);

  private SchemaBuilder() {
    // Utility classes should not have public constructors
  }

  /**
   * Initializes graph database.
   *
   * @param graph The janus graph instance.
   */
  public static void load(final JanusGraph graph) throws InterruptedException {
    final JanusGraphManagement management = graph.openManagement();

    // naive check if the schema was previously created
    if (management.getRelationTypes(RelationType.class).iterator().hasNext()) {
      management.rollback();
      return;
    }
    LOGGER.info(
        "################################ creating schema ################################");
    createProperties(management);
    createVertexLabels(management);
    createEdgeLabels(management);
    management.commit();

    createCompositeIndexes(graph);

    LOGGER.info(
        "########################### finished creating schema ############################");
  }

  private static void createVertexLabels(final JanusGraphManagement management) {
    management.makeVertexLabel("command").make();
    management.makeVertexLabel("usercommand").make();
    management.makeVertexLabel("option").make();
    management.makeVertexLabel("optionvalue").make();
    management.makeVertexLabel("flag").make();
    management.makeVertexLabel("flagvalue").make();
    management.makeVertexLabel("user").make();
  }

  private static void createEdgeLabels(final JanusGraphManagement management) {
    management.makeEdgeLabel("belongs_to").multiplicity(Multiplicity.MANY2ONE).make();
    management.makeEdgeLabel("has_option").multiplicity(Multiplicity.ONE2MANY).make();
    management.makeEdgeLabel("has_flag").multiplicity(Multiplicity.ONE2MANY).make();
  }

  private static void createProperties(final JanusGraphManagement management) {
    management.makePropertyKey("name").dataType(String.class).make();
    management.makePropertyKey("desc").dataType(String.class).make();
    management.makePropertyKey("long_desc").dataType(String.class).make();
    management.makePropertyKey("syntax").dataType(String.class).make();
    management.makePropertyKey("man_page_url").dataType(String.class).make();

    management.makePropertyKey("prefix").dataType(String.class).make();
    management.makePropertyKey("alias").dataType(String.class).make();
    management.makePropertyKey("is_grouping_allowed").dataType(String.class).make();
    management.makePropertyKey("is_solitary").dataType(String.class).make();
    management.makePropertyKey("sequence").dataType(Byte.class).make();

    management.makePropertyKey("data_type").dataType(String.class).make();
    management.makePropertyKey("is_repeatable").dataType(String.class).make();
    management.makePropertyKey("is_mandatory").dataType(String.class).make();

    management.makePropertyKey("user_email").dataType(String.class).make();
    management.makePropertyKey("command_name").dataType(String.class).make();
    management.makePropertyKey("command_text").dataType(String.class).make();
    management.makePropertyKey("timestamp").dataType(String.class).make();

    management.makePropertyKey("email").dataType(String.class).make();
    management.makePropertyKey("email_verified").dataType(String.class).make();
    management.makePropertyKey("password").dataType(String.class).make();
    management.makePropertyKey("provider").dataType(String.class).make();
    management.makePropertyKey("provider_id").dataType(String.class).make();
    management.makePropertyKey("image_url").dataType(String.class).make();

    // Properties which supports multiple values
    management.makePropertyKey("FILE").dataType(String.class).cardinality(Cardinality.SET).make();
  }

  private static void createCompositeIndexes(JanusGraph graph) throws InterruptedException {
    JanusGraphManagement management = graph.openManagement();
    management
        .buildIndex("commandnameindex", Vertex.class)
        .addKey(management.getPropertyKey("name"))
        .indexOnly(management.getVertexLabel("command"))
        .buildCompositeIndex();

    management
        .buildIndex("usercommandnameindex", Vertex.class)
        .addKey(management.getPropertyKey("command_name"))
        .buildCompositeIndex();
    management.commit();

    // Wait for the index to become available
    ManagementSystem.awaitGraphIndexStatus(graph, "commandnameindex").call();
    ManagementSystem.awaitGraphIndexStatus(graph, "usercommandnameindex").call();
  }
}
