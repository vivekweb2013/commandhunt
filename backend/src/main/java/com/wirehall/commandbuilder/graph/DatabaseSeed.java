package com.wirehall.commandbuilder.graph;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * This component is can be to seed the database on startup
 * To execute the seed operation pass the -Dapp.seed-only=true option while executing jar
 * NOTE: After executing the seed operation, The app will be exited
 */
@Component
@ConditionalOnProperty("app.seed-only")
@Order(Ordered.LOWEST_PRECEDENCE)
public class DatabaseSeed implements ApplicationRunner {
    @Autowired
    private GraphBuilder graphBuilder;

    @Override
    public void run(ApplicationArguments args) {
        graphBuilder.initialize();
        System.exit(0);
    }
}
