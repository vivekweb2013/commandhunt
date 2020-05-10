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
    management.makeVertexLabel(VertexType.command.toString()).make();
    management.makeVertexLabel(VertexType.option.toString()).make();
    management.makeVertexLabel(VertexType.flag.toString()).make();
  }

  private static void createEdgeLabels(final JanusGraphManagement management) {
    management.makeEdgeLabel(EdgeType.belongs_to.toString()).multiplicity(Multiplicity.MANY2ONE)
        .make();
    management.makeEdgeLabel(EdgeType.has_option.toString()).multiplicity(Multiplicity.ONE2MANY)
        .make();
    management.makeEdgeLabel(EdgeType.has_flag.toString()).multiplicity(Multiplicity.ONE2MANY)
        .make();
  }

  private static void createProperties(final JanusGraphManagement management) {
    management.makePropertyKey(CommandProperty.name.toString()).dataType(String.class).make();
    management.makePropertyKey(CommandProperty.desc.toString()).dataType(String.class).make();
    management.makePropertyKey(CommandProperty.long_desc.toString()).dataType(String.class).make();
    management.makePropertyKey(CommandProperty.syntax.toString()).dataType(String.class).make();
    management
        .makePropertyKey(CommandProperty.man_page_url.toString())
        .dataType(String.class)
        .make();

    management.makePropertyKey(FlagProperty.prefix.toString()).dataType(String.class).make();
    management.makePropertyKey(FlagProperty.alias.toString()).dataType(String.class).make();
    management
        .makePropertyKey(FlagProperty.is_grouping_allowed.toString())
        .dataType(String.class)
        .make();
    management.makePropertyKey(FlagProperty.sequence.toString()).dataType(Byte.class).make();

    management.makePropertyKey(OptionProperty.data_type.toString()).dataType(String.class).make();
    management
        .makePropertyKey(OptionProperty.is_repeatable.toString())
        .dataType(String.class)
        .make();
    management
        .makePropertyKey(OptionProperty.is_mandatory.toString())
        .dataType(String.class)
        .make();
  }

  private static void createCompositeIndexes(final JanusGraphManagement management) {
    management
        .buildIndex("nameIndex", Vertex.class)
        .addKey(management.getPropertyKey(CommandProperty.name.toString()))
        .buildCompositeIndex();
  }
}
