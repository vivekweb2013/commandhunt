package com.wirehall.commandhunt.backend.service;

import com.wirehall.commandhunt.backend.dto.UserCommand;
import com.wirehall.commandhunt.backend.dto.filter.Filter;
import com.wirehall.commandhunt.backend.dto.filter.PageResponse;
import com.wirehall.commandhunt.backend.mapper.PaginationMapper;
import com.wirehall.commandhunt.backend.mapper.UserCommandMapper;
import com.wirehall.commandhunt.backend.model.UserCommandEntity;
import com.wirehall.commandhunt.backend.repository.UserCommandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCommandService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserCommandService.class);

  private final UserCommandMapper mapper = new UserCommandMapper();
  private final PaginationMapper paginationMapper = new PaginationMapper();
  private final UserCommandRepository userCommandRepository;

  @Autowired
  public UserCommandService(UserCommandRepository userCommandRepository) {
    this.userCommandRepository = userCommandRepository;
  }

  /**
   * Retrieves all the user commands.
   *
   * @return The list of user commands.
   */
  public List<UserCommand> getAllUserCommands() {
    LOGGER.debug("Retrieving all user-commands");
    List<UserCommandEntity> userCommandEntity = userCommandRepository.findAll();
    return userCommandEntity.stream().map(mapper::mapToUserCommand).collect(Collectors.toList());
  }

  /**
   * Retrieves all the user commands matching the filter criteria.
   *
   * @param filter Filter criteria.
   * @return Page of user-command DTOs.
   */
  public PageResponse<UserCommand> getAllUserCommands(Filter filter) {
    LOGGER.debug("Retrieving all user-commands");
    LOGGER.debug("Applying the filter: {}", filter);

    Pageable pageable = paginationMapper.mapToPageable(filter);
    Page<UserCommandEntity> userCommandEntityPage = userCommandRepository.findAll(pageable);
    return mapper.mapToPageResponse(userCommandEntityPage, filter);
  }

  /**
   * Retrieves the user-command using id.
   *
   * @param userCommandId The id of the user-command to retrieve.
   * @return The user-command dto.
   */
  public UserCommand getUserCommandById(Long userCommandId) {
    LOGGER.debug("Retrieving user-command with id: {}", userCommandId);

    UserCommandEntity userCommandEntity = userCommandRepository.getOne(userCommandId);
    return mapper.mapToUserCommand(userCommandEntity);
  }

  /**
   * Adds the new user-command.
   *
   * @param userCommand The user-command dto to be added.
   */
  public void addUserCommand(UserCommand userCommand) {
    LOGGER.info("Inserting user-command {}", userCommand);

    userCommand.setTimestamp(new Timestamp(System.currentTimeMillis()));
    UserCommandEntity userCommandEntity = mapper.mapToUserCommandEntity(userCommand);
    userCommandRepository.save(userCommandEntity);
  }

  /**
   * Updates the existing user-command.
   *
   * @param userCommand The user-command dto to be updated.
   */
  public void updateUserCommand(UserCommand userCommand) {
    LOGGER.info("Updating user-command {}", userCommand);

    userCommand.setTimestamp(new Timestamp(System.currentTimeMillis()));
    UserCommandEntity userCommandEntity = mapper.mapToUserCommandEntity(userCommand);
    userCommandRepository.save(userCommandEntity);
  }

  /**
   * Deletes the user-command using id.
   *
   * @param userCommandId The id of user-command to be deleted.
   */
  public void deleteUserCommand(Long userCommandId) {
    LOGGER.info("Updating user-command with id {}", userCommandId);

    userCommandRepository.deleteById(userCommandId);
  }
}
