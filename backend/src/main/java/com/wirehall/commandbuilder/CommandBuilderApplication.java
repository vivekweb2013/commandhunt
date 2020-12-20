package com.wirehall.commandbuilder;

import com.wirehall.commandbuilder.graph.GraphBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication
public class CommandBuilderApplication {

  private static final Logger LOGGER = LoggerFactory.getLogger(CommandBuilderApplication.class);
  private final GraphBuilder graphBuilder;

  @Autowired
  private Environment env;

  @Autowired
  public CommandBuilderApplication(GraphBuilder graphBuilder) {
    this.graphBuilder = graphBuilder;
  }

  public static void main(String[] args) {
    SpringApplication.run(CommandBuilderApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void init() {
    if (Arrays.asList(env.getActiveProfiles()).contains("dev")) {
      try {
        graphBuilder.initialize();
      } catch (Exception e) {
        LOGGER.error(e.getMessage(), e);
      }
    }
  }
}
