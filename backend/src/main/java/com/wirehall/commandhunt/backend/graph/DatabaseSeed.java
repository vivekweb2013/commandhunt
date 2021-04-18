package com.wirehall.commandhunt.backend.graph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/** This component is can be to seed the database on startup. */
@Component
public class DatabaseSeed implements ApplicationRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseSeed.class);
  private final MetadataManager metadataManager;

  @Autowired
  public DatabaseSeed(MetadataManager metadataManager) {
    this.metadataManager = metadataManager;
  }

  @Override
  public void run(ApplicationArguments args) {
    try {
      metadataManager.load();
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      // Exit with error status code
      System.exit(1);
    }
  }
}
