package com.wirehall.commandbuilder;

import com.wirehall.commandbuilder.graph.GraphBuilder;
import org.apache.commons.configuration.ConfigurationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class CommandBuilderApplication {

    @EventListener(ApplicationReadyEvent.class)
    public void init() throws ConfigurationException {
        GraphBuilder graphInitializer = new GraphBuilder("jg-inmemory.properties");
    }

    public static void main(String[] args) {
        SpringApplication.run(CommandBuilderApplication.class, args);
    }

}
