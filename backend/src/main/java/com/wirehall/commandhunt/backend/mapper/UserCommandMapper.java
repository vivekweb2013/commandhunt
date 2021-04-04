package com.wirehall.commandhunt.backend.mapper;


import com.wirehall.commandhunt.backend.dto.UserCommand;
import com.wirehall.commandhunt.backend.model.UserCommandEntity;

public final class UserCommandMapper {

  /**
   * Maps user-command entity to user-command dto
   *
   * @param userCommandEntity To be mapped to user-command dto.
   * @param mapAssociations   This flag should be set to false if entity's associations are not required.
   *                          Since un-necessarily mapping this will invoke lazy loading of these associations.
   * @return The user-command dto.
   */
  public UserCommand mapToUserCommand(UserCommandEntity userCommandEntity, boolean mapAssociations) {
    UserCommand userCommand = new UserCommand();
    userCommand.setId(userCommandEntity.getId());

    userCommand.setCommandName(userCommandEntity.getCommandName());
    userCommand.setCommandText(userCommandEntity.getCommandText());
    userCommand.setCreatedOn(userCommandEntity.getCreatedOn());
    userCommand.setModifiedOn(userCommandEntity.getModifiedOn());
    userCommand.setOperatedOn(userCommandEntity.getOperatedOn());

    if (mapAssociations) {
      userCommand.setFlags(userCommandEntity.getFlags());
      userCommand.setOptions(userCommandEntity.getOptions());
    }
    return userCommand;
  }

  /**
   * Maps user-command dto to user-command entity
   *
   * @param userCommand The user-command dto.
   * @param userEmail   Logged in user's email id.
   * @return The user-command entity.
   */
  public UserCommandEntity mapToUserCommandEntity(UserCommand userCommand, String userEmail) {
    UserCommandEntity userCommandEntity = new UserCommandEntity();
    userCommandEntity.setId(userCommand.getId());
    userCommandEntity.setUserEmail(userEmail);
    userCommandEntity.setCommandName(userCommand.getCommandName());
    userCommandEntity.setCommandText(userCommand.getCommandText());
    userCommandEntity.setCreatedOn(userCommand.getCreatedOn());
    userCommandEntity.setModifiedOn(userCommand.getModifiedOn());
    userCommandEntity.setOperatedOn(userCommand.getOperatedOn());

    userCommandEntity.setFlags(userCommand.getFlags());
    userCommandEntity.setOptions(userCommand.getOptions());
    return userCommandEntity;
  }
}
