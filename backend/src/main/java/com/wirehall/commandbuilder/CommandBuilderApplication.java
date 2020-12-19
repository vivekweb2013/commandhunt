package com.wirehall.commandbuilder;

import com.wirehall.commandbuilder.graph.GraphBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication
public class CommandBuilderApplication {

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
      graphBuilder.initialize();
    }
  }
}
