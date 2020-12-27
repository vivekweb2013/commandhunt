package com.wirehall.commandhunt.graph;

import com.wirehall.commandhunt.model.EdgeType;
import com.wirehall.commandhunt.model.VertexType;
import com.wirehall.commandhunt.model.props.*;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.Cardinality;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.Multiplicity;
import org.janusgraph.core.RelationType;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.graphdb.database.management.ManagementSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class SchemaManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(SchemaManager.class);

  private SchemaManager() {
    // Utility classes should not have public constructors
  }

  /**
   * Creates the application's graph db schema.
   *
   * @param graph The janus graph instance.
   */
  public static void createSchema(final JanusGraph graph) throws InterruptedException {
    final JanusGraphManagement management = graph.openManagement();
    String indexBackend = graph.configuration().getString("index.search.backend");

    // naive check if the schema was previously created
    if (management.getRelationTypes(RelationType.class).iterator().hasNext()) {
      management.rollback();
      return;
    }
    LOGGER.info("################################ creating schema ################################");
    createProperties(management);
    createVertexLabels(management);
    createEdgeLabels(management);
    management.commit();

    createCompositeIndexes(graph);
    if (StringUtils.hasText(indexBackend)) {
      // Execute only if index backend is configured
      createMixedIndexes(graph);
    }

    LOGGER.info("########################### finished creating schema ############################");
  }

  private static void createVertexLabels(final JanusGraphManagement management) {
    management.makeVertexLabel(VertexType.COMMAND.toLowerCase()).make();
    management.makeVertexLabel(VertexType.USERCOMMAND.toLowerCase()).make();
    management.makeVertexLabel(VertexType.OPTION.toLowerCase()).make();
    management.makeVertexLabel(VertexType.OPTIONVALUE.toLowerCase()).make();
    management.makeVertexLabel(VertexType.FLAG.toLowerCase()).make();
    management.makeVertexLabel(VertexType.FLAGVALUE.toLowerCase()).make();
    management.makeVertexLabel(VertexType.USER.toLowerCase()).make();
  }

  private static void createEdgeLabels(final JanusGraphManagement management) {
    management.makeEdgeLabel(EdgeType.BELONGS_TO.toLowerCase()).multiplicity(Multiplicity.MANY2ONE)
        .make();
    management.makeEdgeLabel(EdgeType.HAS_OPTION.toLowerCase()).multiplicity(Multiplicity.ONE2MANY)
        .make();
    management.makeEdgeLabel(EdgeType.HAS_FLAG.toLowerCase()).multiplicity(Multiplicity.ONE2MANY)
        .make();
  }

  private static void createProperties(final JanusGraphManagement management) {
    management.makePropertyKey(CommandProperty.NAME.toLowerCase()).dataType(String.class).make();
    management.makePropertyKey(CommandProperty.DESC.toLowerCase()).dataType(String.class).make();
    management.makePropertyKey(CommandProperty.LONG_DESC.toLowerCase()).dataType(String.class).make();
    management.makePropertyKey(CommandProperty.SYNTAX.toLowerCase()).dataType(String.class).make();
    management.makePropertyKey(CommandProperty.MAN_PAGE_URL.toLowerCase()).dataType(String.class).make();

    management.makePropertyKey(FlagProperty.PREFIX.toLowerCase()).dataType(String.class).make();
    management.makePropertyKey(FlagProperty.ALIAS.toLowerCase()).dataType(String.class).make();
    management
            .makePropertyKey(FlagProperty.IS_GROUPING_ALLOWED.toLowerCase())
            .dataType(String.class)
            .make();
    management
            .makePropertyKey(FlagProperty.IS_SOLITARY.toLowerCase())
            .dataType(String.class)
            .make();
    management.makePropertyKey(FlagProperty.SEQUENCE.toLowerCase()).dataType(Byte.class).make();

    management.makePropertyKey(OptionProperty.DATA_TYPE.toLowerCase()).dataType(String.class)
            .make();
    management
            .makePropertyKey(OptionProperty.IS_REPEATABLE.toLowerCase())
            .dataType(String.class)
            .make();
    management
            .makePropertyKey(OptionProperty.IS_MANDATORY.toLowerCase())
            .dataType(String.class)
            .make();

    management.makePropertyKey(UserCommandProperty.USER_EMAIL.toLowerCase()).dataType(String.class).make();
    management.makePropertyKey(UserCommandProperty.COMMAND_NAME.toLowerCase()).dataType(String.class).make();
    management.makePropertyKey(UserCommandProperty.COMMAND_TEXT.toLowerCase()).dataType(String.class).make();
    management.makePropertyKey(UserCommandProperty.TIMESTAMP.toLowerCase()).dataType(String.class).make();

    management.makePropertyKey(UserProperty.EMAIL.toLowerCase()).dataType(String.class).make();
    management.makePropertyKey(UserProperty.EMAIL_VERIFIED.toLowerCase()).dataType(String.class).make();
    management.makePropertyKey(UserProperty.PASSWORD.toLowerCase()).dataType(String.class).make();
    management.makePropertyKey(UserProperty.PROVIDER.toLowerCase()).dataType(String.class).make();
    management.makePropertyKey(UserProperty.PROVIDER_ID.toLowerCase()).dataType(String.class).make();
    management.makePropertyKey(UserProperty.IMAGE_URL.toLowerCase()).dataType(String.class).make();

    // Properties which supports multiple values
    management.makePropertyKey("FILE").dataType(String.class).cardinality(Cardinality.SET).make();
  }

  private static void createCompositeIndexes(JanusGraph graph) throws InterruptedException {
    JanusGraphManagement mgmt = graph.openManagement();
    mgmt
            .buildIndex("commandnameindex", Vertex.class)
            .addKey(mgmt.getPropertyKey(CommandProperty.NAME.toLowerCase()))
            .indexOnly(mgmt.getVertexLabel("command"))
            .buildCompositeIndex();

    mgmt
            .buildIndex("usercommandnameindex", Vertex.class)
            .addKey(mgmt.getPropertyKey(UserCommandProperty.COMMAND_NAME.toLowerCase()))
            .buildCompositeIndex();
    mgmt.commit();

    // Wait for the index to become available
    ManagementSystem.awaitGraphIndexStatus(graph, "commandnameindex").call();
    ManagementSystem.awaitGraphIndexStatus(graph, "usercommandnameindex").call();
  }

  private static void createMixedIndexes(JanusGraph graph) throws InterruptedException {
    JanusGraphManagement mgmt = graph.openManagement();
    mgmt
            .buildIndex("commandnamemixedindex", Vertex.class)
            .addKey(mgmt.getPropertyKey(CommandProperty.NAME.toLowerCase()))
            .indexOnly(mgmt.getVertexLabel("command"))
            .buildMixedIndex("search");
    mgmt
            .buildIndex("commandnamedescmixedindex", Vertex.class)
            .addKey(mgmt.getPropertyKey(CommandProperty.NAME.toLowerCase()))
            .addKey(mgmt.getPropertyKey(CommandProperty.DESC.toLowerCase()))
            .addKey(mgmt.getPropertyKey(CommandProperty.LONG_DESC.toLowerCase()))
            .buildMixedIndex("search");
    mgmt.commit();

    // Wait for the index to become available
    ManagementSystem.awaitGraphIndexStatus(graph, "commandnamemixedindex").call();
    ManagementSystem.awaitGraphIndexStatus(graph, "commandnamedescmixedindex").call();
  }
}
