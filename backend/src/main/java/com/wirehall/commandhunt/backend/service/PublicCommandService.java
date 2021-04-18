package com.wirehall.commandhunt.backend.service;

import com.wirehall.commandhunt.backend.dto.PublicCommand;
import com.wirehall.commandhunt.backend.exception.BadRequestException;
import com.wirehall.commandhunt.backend.mapper.PublicCommandMapper;
import com.wirehall.commandhunt.backend.model.PublicCommandEntity;
import com.wirehall.commandhunt.backend.repository.PublicCommandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;

@Service
@Transactional
public class PublicCommandService {
  private static final Logger LOGGER = LoggerFactory.getLogger(PublicCommandService.class);

  private final PublicCommandRepository publicCommandRepository;
  private final PublicCommandMapper mapper = new PublicCommandMapper();

  @Autowired
  public PublicCommandService(PublicCommandRepository publicCommandRepository) {
    this.publicCommandRepository = publicCommandRepository;
  }

  /**
   * Retrieves the public-command using id.
   *
   * @param publicCommandId The id of the requested public-command.
   * @param userEmail The email of logged in user if any, otherwise null.
   * @return The public-command with deletable indicator.
   */
  public PublicCommand getPublicCommandById(Long publicCommandId, String userEmail) {
    PublicCommandEntity entity = publicCommandRepository.getOne(publicCommandId);
    LOGGER.debug("Retrieved public-command entity: {}", entity);
    PublicCommand publicCommand = mapper.mapToPublicCommand(entity, true);
    publicCommand.setDeletable(entity.getUserEmail().equals(userEmail));
    return publicCommand;
  }

  /**
   * Adds the new public-command.
   *
   * @param publicCommand The public-command dto to be added.
   * @param userEmail Logged-in user's email id.
   * @return The newly added public-command.
   */
  public PublicCommand addPublicCommand(PublicCommand publicCommand, String userEmail) {
    if (publicCommand.getId() != null) {
      throw new BadRequestException("Invalid save operation. Not a new public-command.");
    }
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    publicCommand.setCreatedOn(timestamp);
    PublicCommandEntity entity = mapper.mapToPublicCommandEntity(publicCommand, userEmail);
    LOGGER.info("Inserting public-command entity: {}", entity);
    publicCommandRepository.save(entity);
    LOGGER.info("Inserted public-command entity with id: {}", entity.getId());
    return mapper.mapToPublicCommand(entity, false);
  }

  /**
   * Deletes the public-command using id.
   *
   * @param publicCommandId The id of public-command to be deleted.
   * @param userEmail Logged-in user's email id.
   */
  public void deletePublicCommand(Long publicCommandId, String userEmail) {
    publicCommandRepository.deleteByIdAndUserEmail(publicCommandId, userEmail);
    LOGGER.info("Deleted public-command entity having id: {}", publicCommandId);
  }
}
