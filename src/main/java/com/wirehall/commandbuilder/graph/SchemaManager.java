package com.wirehall.commandbuilder.graph;

import com.wirehall.commandbuilder.model.EdgeType;
import com.wirehall.commandbuilder.model.VertexType;
import com.wirehall.commandbuilder.model.props.CommandProperty;
import com.wirehall.commandbuilder.model.props.FlagProperty;
import com.wirehall.commandbuilder.model.props.OptionProperty;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.Multiplicity;
import org.janusgraph.core.RelationType;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
  public static void createSchema(final JanusGraph graph) {
    final JanusGraphManagement management = graph.openManagement();
    try {
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
      createCompositeIndexes(management);
      management.commit();
      LOGGER.info(
          "########################### finished creating schema ############################");
    } catch (Exception e) {
      management.rollback();
      throw e;
    }
  }

  private static void createVertexLabels(final JanusGraphManagement management) {
    management.makeVertexLabel(VertexType.COMMAND.toLowerCase()).make();
    management.makeVertexLabel(VertexType.OPTION.toLowerCase()).make();
    management.makeVertexLabel(VertexType.FLAG.toLowerCase()).make();
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
    management.makePropertyKey(CommandProperty.LONG_DESC.toLowerCase()).dataType(String.class)
        .make();
    management.makePropertyKey(CommandProperty.SYNTAX.toLowerCase()).dataType(String.class).make();
    management
        .makePropertyKey(CommandProperty.MAN_PAGE_URL.toLowerCase())
        .dataType(String.class)
        .make();

    management.makePropertyKey(FlagProperty.PREFIX.toLowerCase()).dataType(String.class).make();
    management.makePropertyKey(FlagProperty.ALIAS.toLowerCase()).dataType(String.class).make();
    management
        .makePropertyKey(FlagProperty.IS_GROUPING_ALLOWED.toLowerCase())
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
  }

  private static void createCompositeIndexes(final JanusGraphManagement management) {
    management
        .buildIndex("nameIndex", Vertex.class)
        .addKey(management.getPropertyKey(CommandProperty.NAME.toLowerCase()))
        .buildCompositeIndex();
  }
}
