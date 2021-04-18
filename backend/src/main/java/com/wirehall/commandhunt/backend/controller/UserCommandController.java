package com.wirehall.commandhunt.backend.controller;

import com.wirehall.commandhunt.backend.dto.UserCommand;
import com.wirehall.commandhunt.backend.dto.filter.Filter;
import com.wirehall.commandhunt.backend.dto.filter.PageResponse;
import com.wirehall.commandhunt.backend.service.UserCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping(value = "api/user", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('ROLE_USER')")
public class UserCommandController {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserCommandController.class);

  private final UserCommandService userCommandService;

  @Autowired
  public UserCommandController(UserCommandService userCommandService) {
    this.userCommandService = userCommandService;
  }

  @GetMapping(value = "/user-command")
  public PageResponse<UserCommand> getAllUserCommands(@Valid Filter filter, Principal principal) {
    LOGGER.info("Request from user: {} for retrieving user-commands", principal.getName());
    LOGGER.info("Received filters: {}", filter);
    return userCommandService.getAllUserCommands(filter, principal.getName());
  }

  @GetMapping(value = "/user-command/{id}")
  public UserCommand getUserCommandById(@PathVariable(name = "id") Long id, Principal principal) {
    LOGGER.info(
        "Request from user: {} for retrieving user-command with id: {}", principal.getName(), id);
    return userCommandService.getUserCommandById(id, principal.getName());
  }

  @PostMapping(value = "/user-command")
  public void addUserCommand(@RequestBody UserCommand userCommand, Principal principal) {
    LOGGER.info(
        "Request from user: {} for adding user-command: {}", principal.getName(), userCommand);
    userCommandService.addUserCommand(userCommand, principal.getName());
  }

  @PutMapping(value = "/user-command/{id}")
  public void updateUserCommand(@RequestBody UserCommand userCommand, Principal principal) {
    LOGGER.info(
        "Request from user: {} for updating user-command: {}", principal.getName(), userCommand);
    userCommandService.updateUserCommand(userCommand, principal.getName());
  }

  @DeleteMapping(value = "/user-command/{id}")
  public void deleteUserCommand(
      @PathVariable(name = "id") Long userCommandId, Principal principal) {
    LOGGER.info(
        "Request from user: {} for deleting user-command with id: {}",
        principal.getName(),
        userCommandId);
    userCommandService.deleteUserCommand(userCommandId, principal.getName());
  }
}
