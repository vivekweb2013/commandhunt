package com.wirehall.commandhunt.graph;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.util.GraphFactory;
import org.janusgraph.core.JanusGraph;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphConfiguration {

  @Value("${app.graph.config}")
  private String appGraphConfigPath;

  @Bean
  JanusGraph janusGraph() throws ConfigurationException {
    return (JanusGraph) GraphFactory.open(new PropertiesConfiguration(appGraphConfigPath));
  }

  @Bean
  GraphTraversalSource graphTraversalSource(JanusGraph janusGraph) {
    return janusGraph.traversal();
  }
}
