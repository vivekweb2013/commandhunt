package com.wirehall.commandbuilder.graph;

import com.wirehall.commandbuilder.model.*;
import com.wirehall.commandbuilder.model.props.COMMAND_PROPERTY;
import com.wirehall.commandbuilder.model.props.FLAG_PROPERTY;
import com.wirehall.commandbuilder.model.props.OPTION_PROPERTY;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.Multiplicity;
import org.janusgraph.core.RelationType;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchemaManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaManager.class);

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
        management.makePropertyKey(COMMAND_PROPERTY.name.toString()).dataType(String.class).make();
        management.makePropertyKey(COMMAND_PROPERTY.desc.toString()).dataType(String.class).make();
        management.makePropertyKey(FLAG_PROPERTY.prefix.toString()).dataType(String.class).make();
        management.makePropertyKey(FLAG_PROPERTY.sequence.toString()).dataType(Byte.class).make();
        management.makePropertyKey(OPTION_PROPERTY.data_type.toString()).dataType(DATA_TYPE.class).make();
        management.makePropertyKey(OPTION_PROPERTY.is_mandatory.toString()).dataType(Boolean.class).make();
        management.makePropertyKey(OPTION_PROPERTY.is_repeatable.toString()).dataType(Boolean.class).make();
    }

    private static void createCompositeIndexes(final JanusGraphManagement management) {
        management.buildIndex("nameIndex", Vertex.class).addKey(management.getPropertyKey(COMMAND_PROPERTY.name.toString())).buildCompositeIndex();
    }

}
