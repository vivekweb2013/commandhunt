package com.wirehall.commandhunt.backend.graph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * This component is can be to seed the database on startup
 * To execute the seed operation pass the -Dapp.seed-only=true option while executing jar
 * <p>
 * NOTE: After executing the seed operation, The app will be exited,
 * Since the purpose of this bean is to seed the database with k8s job
 * and exit with either success or error status code
 */
@Component
@ConditionalOnProperty("app.seed-only")
public class DatabaseSeed implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseSeed.class);

    @Autowired
    private GraphBuilder graphBuilder;

    @Override
    public void run(ApplicationArguments args) {
        try {
            graphBuilder.initialize();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            // Exit with error status code
            System.exit(1);
        }

        System.exit(0);
    }
}
