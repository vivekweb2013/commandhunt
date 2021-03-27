package com.wirehall.commandhunt.backend.service;

import com.wirehall.commandhunt.backend.dto.UserCommand;
import com.wirehall.commandhunt.backend.dto.filter.Condition;
import com.wirehall.commandhunt.backend.dto.filter.Filter;
import com.wirehall.commandhunt.backend.dto.filter.PageResponse;
import com.wirehall.commandhunt.backend.mapper.PaginationMapper;
import com.wirehall.commandhunt.backend.mapper.UserCommandMapper;
import com.wirehall.commandhunt.backend.model.UserCommandEntity;
import com.wirehall.commandhunt.backend.repository.UserCommandRepository;
import com.wirehall.commandhunt.backend.repository.UserCommandSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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
    List<UserCommandEntity> userCommandEntity = userCommandRepository.findAll();
    LOGGER.debug("Retrieved {} user-command entities", userCommandEntity.size());
    return userCommandEntity.stream().map(mapper::mapToUserCommand).collect(Collectors.toList());
  }

  /**
   * Retrieves all the user commands matching the filter criteria.
   *
   * @param filter Filter criteria.
   * @param userEmail Logged-in user's email id.
   * @return Page of user-command DTOs.
   */
  public PageResponse<UserCommand> getAllUserCommands(Filter filter, String userEmail) {
    Pageable pageable = paginationMapper.mapToPageable(filter);
    List<Condition> conditions = filter.getConditions();
    Specification<UserCommandEntity> specification = Specification.where(UserCommandSpecification.equalsUserEmail(userEmail));

    Specification<UserCommandEntity> optionalSpecification = null;
    for (Condition c : conditions) {

      if (c.getKey().equalsIgnoreCase("commandName") && c.getOperator().name().equals("EQUALS")) {
        Specification<UserCommandEntity> s = UserCommandSpecification.equalsCommandName(c.getValue());
        optionalSpecification = optionalSpecification == null ? s : optionalSpecification.or(s);
      } else if (c.getKey().equalsIgnoreCase("commandText") && c.getOperator().name().equals("CONTAINS")) {
        Specification<UserCommandEntity> s = UserCommandSpecification.likeCommandText(c.getValue());
        optionalSpecification = optionalSpecification == null ? s : optionalSpecification.or(s);
      }
    }

    specification = specification.and(optionalSpecification);
    Page<UserCommandEntity> userCommandEntityPage = userCommandRepository.findAll(specification, pageable);
    LOGGER.debug("Retrieved {} user-command entities", userCommandEntityPage.getTotalElements());
    return mapper.mapToPageResponse(userCommandEntityPage, filter);
  }

  /**
   * Retrieves the user-command using id.
   *
   * @param userCommandId The id of the user-command to retrieve.
   * @param userEmail Logged-in user's email id.
   * @return The user-command dto.
   */
  public UserCommand getUserCommandById(Long userCommandId, String userEmail) {
    UserCommandEntity userCommandEntity = userCommandRepository.findOneByIdAndUserEmail(userCommandId, userEmail);
    LOGGER.debug("Retrieved user-command entity: {}", userCommandEntity);
    return mapper.mapToUserCommand(userCommandEntity);
  }

  /**
   * Adds the new user-command.
   *
   * @param userCommand The user-command dto to be added.
   * @param userEmail Logged-in user's email id.
   */
  public void addUserCommand(UserCommand userCommand, String userEmail) {
    userCommand.setTimestamp(new Timestamp(System.currentTimeMillis()));
    UserCommandEntity userCommandEntity = mapper.mapToUserCommandEntity(userCommand, userEmail);
    LOGGER.info("Inserting user-command entity: {}", userCommandEntity);
    userCommandRepository.save(userCommandEntity);
    LOGGER.info("Inserted user-command entity with id: {}", userCommandEntity.getId());
  }

  /**
   * Updates the existing user-command.
   *
   * @param userCommand The user-command dto to be updated.
   * @param userEmail Logged-in user's email id.
   */
  public void updateUserCommand(UserCommand userCommand, String userEmail) {
    userCommand.setTimestamp(new Timestamp(System.currentTimeMillis()));
    UserCommandEntity userCommandEntity = mapper.mapToUserCommandEntity(userCommand, userEmail);
    LOGGER.info("Updating user-command entity: {}", userCommandEntity);
    userCommandRepository.save(userCommandEntity);
    LOGGER.info("Updated user-command entity having id: {}", userCommandEntity.getId());
  }

  /**
   * Deletes the user-command using id.
   *
   * @param userCommandId The id of user-command to be deleted.
   * @param userEmail Logged-in user's email id.
   */
  public void deleteUserCommand(Long userCommandId, String userEmail) {
    userCommandRepository.deleteByIdAndUserEmail(userCommandId, userEmail);
    LOGGER.info("Deleted user-command entity having id: {}", userCommandId);
  }
}
