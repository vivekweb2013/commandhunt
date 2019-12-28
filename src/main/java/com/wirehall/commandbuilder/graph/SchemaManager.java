package com.wirehall.commandbuilder.graph;

import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.Multiplicity;
import org.janusgraph.core.RelationType;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchemaManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaManager.class);

    public enum VERTEX {command, option, flag}

    public enum EDGE {belongs_to, has_option, has_flag, overrides}

    public enum PROPERTIES {name, alias, desc, long_desc, prefix, sequence, type, is_mandatory, is_repeatable, is_groupable}

    public enum TYPE {
        PATH, FILE_NAME, NUMERIC_PERMISSION, NUMBER, STRING;
    }

    public static void createSchema(final JanusGraph graph) {
        final JanusGraphManagement management = graph.openManagement();
        try {
            // naive check if the schema was previously created
            if (management.getRelationTypes(RelationType.class).iterator().hasNext()) {
                management.rollback();
                return;
            }
            LOGGER.info("################################ creating schema ################################");
            createProperties(management);
            createVertexLabels(management);
            createEdgeLabels(management);
            createCompositeIndexes(management);
            management.commit();
            LOGGER.info("########################### finished creating schema ############################");
        } catch (Exception e) {
            management.rollback();
        }
    }

    private static void createVertexLabels(final JanusGraphManagement management) {
        management.makeVertexLabel(VERTEX.command.toString()).make();
        management.makeVertexLabel(VERTEX.option.toString()).make();
        management.makeVertexLabel(VERTEX.flag.toString()).make();
    }

    private static void createEdgeLabels(final JanusGraphManagement management) {
        management.makeEdgeLabel(EDGE.belongs_to.toString()).multiplicity(Multiplicity.MANY2ONE).make();
        management.makeEdgeLabel(EDGE.has_option.toString()).multiplicity(Multiplicity.ONE2MANY).make();
        management.makeEdgeLabel(EDGE.has_flag.toString()).multiplicity(Multiplicity.ONE2MANY).make();
    }

    private static void createProperties(final JanusGraphManagement management) {
        management.makePropertyKey(PROPERTIES.name.toString()).dataType(String.class).make();
        management.makePropertyKey(PROPERTIES.desc.toString()).dataType(String.class).make();
        management.makePropertyKey(PROPERTIES.prefix.toString()).dataType(String.class).make();
        management.makePropertyKey(PROPERTIES.sequence.toString()).dataType(Byte.class).make();
        management.makePropertyKey(PROPERTIES.type.toString()).dataType(TYPE.class).make();
        management.makePropertyKey(PROPERTIES.is_mandatory.toString()).dataType(Boolean.class).make();
        management.makePropertyKey(PROPERTIES.is_repeatable.toString()).dataType(Boolean.class).make();
        management.makePropertyKey(PROPERTIES.is_groupable.toString()).dataType(Boolean.class).make();
    }

    public static void createCompositeIndexes(final JanusGraphManagement management) {
        management.buildIndex("nameIndex", Vertex.class).addKey(management.getPropertyKey(PROPERTIES.name.toString())).buildCompositeIndex();
    }
}
