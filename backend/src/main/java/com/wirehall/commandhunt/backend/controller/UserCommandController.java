package com.wirehall.commandhunt.backend.controller;

import com.wirehall.commandhunt.backend.dto.UserCommand;
import com.wirehall.commandhunt.backend.dto.filter.Filter;
import com.wirehall.commandhunt.backend.service.UserCommandService;
import com.wirehall.commandhunt.backend.dto.filter.Page;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserCommandController {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserCommandController.class);

  private final UserCommandService userCommandService;

  @Autowired
  public UserCommandController(UserCommandService userCommandService) {
    this.userCommandService = userCommandService;
  }

  @GetMapping(value = "/user-command")
  public Page<UserCommand> getAllUserCommands(@Valid Filter filter) {
    LOGGER.info("Retrieving all the user-commands");
    return userCommandService.getAllUserCommands(filter);
  }

  @GetMapping(value = "/user-command/{id}")
  public UserCommand getUserCommandById(@PathVariable(name = "id") String id) {
    LOGGER.info("Retrieving user-command with id: {}", id);
    return userCommandService.getUserCommandById(id);
  }

  @PostMapping(value = "/user-command")
  public void addUserCommand(@RequestBody UserCommand userCommand) {
    LOGGER.info("Adding user-command: {}", userCommand);
    userCommandService.addUserCommand(userCommand);
  }

  @PutMapping(value = "/user-command/{id}")
  public void updateUserCommand(@RequestBody UserCommand userCommand) {
    LOGGER.info("Updating user-command: {}", userCommand);
    userCommandService.updateUserCommand(userCommand);
  }

  @DeleteMapping(value = "/user-command/{id}")
  public void deleteUserCommand(@PathVariable(name = "id") String userCommandId) {
    LOGGER.info("Deleting user-command with id: {}", userCommandId);
    userCommandService.deleteUserCommand(userCommandId);
  }
}
