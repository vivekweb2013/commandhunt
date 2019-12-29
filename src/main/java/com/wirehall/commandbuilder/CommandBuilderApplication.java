package com.wirehall.commandbuilder;

import com.wirehall.commandbuilder.graph.GraphBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class CommandBuilderApplication {
    private GraphBuilder graphBuilder;

    @Autowired
    public CommandBuilderApplication(GraphBuilder graphBuilder) {
        this.graphBuilder = graphBuilder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        graphBuilder.initialize();
    }

    public static void main(String[] args) {
        SpringApplication.run(CommandBuilderApplication.class, args);
    }
}
