package com.wirehall.commandbuilder.service;

import com.wirehall.commandbuilder.dto.UserCommand;
import com.wirehall.commandbuilder.dto.filter.Filter;
import com.wirehall.commandbuilder.dto.filter.Page;
import com.wirehall.commandbuilder.model.props.UserCommandProperty;
import com.wirehall.commandbuilder.repository.UserCommandRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCommandService {

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
    return userCommandRepository.getAllUserCommands();
  }

  /**
   * Retrieves all the user commands matching the filter criteria.
   *
   * @param filter Filter criteria.
   * @return Page of user-command DTOs.
   */
  public Page<UserCommand> getAllUserCommands(Filter filter) {
    return userCommandRepository.getAllUserCommands(filter);
  }

  /**
   * Retrieves the user-command using id.
   *
   * @param userCommandId The id of the user-command to retrieve.
   * @return The user-command dto.
   */
  public UserCommand getUserCommandById(String userCommandId) {
    return userCommandRepository.getUserCommandById(userCommandId);
  }

  /**
   * Adds the new user-command.
   *
   * @param userCommand The user-command dto to be added.
   */
  public void addUserCommand(UserCommand userCommand) {
    userCommand.addProperty(UserCommandProperty.TIMESTAMP, System.currentTimeMillis());
    userCommandRepository.addUserCommand(userCommand);
  }

  /**
   * Updates the existing user-command.
   *
   * @param userCommand The user-command dto to be updated.
   */
  public void updateUserCommand(UserCommand userCommand) {
    userCommand.addProperty(UserCommandProperty.TIMESTAMP, System.currentTimeMillis());
    userCommandRepository.updateUserCommand(userCommand);
  }

  /**
   * Deletes the user-command using id.
   *
   * @param userCommandId The id of user-command to be deleted.
   */
  public void deleteUserCommand(String userCommandId) {
    userCommandRepository.deleteUserCommand(userCommandId);
  }
}
