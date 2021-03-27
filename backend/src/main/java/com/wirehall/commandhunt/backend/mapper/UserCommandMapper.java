package com.wirehall.commandhunt.backend.mapper;


import com.wirehall.commandhunt.backend.dto.UserCommand;
import com.wirehall.commandhunt.backend.dto.filter.Filter;
import com.wirehall.commandhunt.backend.dto.filter.PageResponse;
import com.wirehall.commandhunt.backend.model.UserCommandEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public final class UserCommandMapper extends BaseMapper {


  public UserCommand mapToUserCommand(UserCommandEntity userCommandEntity) {
    UserCommand userCommand = new UserCommand();
    userCommand.setId(userCommandEntity.getId());

    userCommand.setFlags(userCommandEntity.getFlags());
    userCommand.setOptions(userCommandEntity.getOptions());
    userCommand.setCommandName(userCommandEntity.getCommandName());
    userCommand.setCommandText(userCommandEntity.getCommandText());
    userCommand.setTimestamp(userCommandEntity.getTimestamp());
    return userCommand;
  }

  public UserCommandEntity mapToUserCommandEntity(UserCommand userCommand, String userEmail) {
    UserCommandEntity userCommandEntity = new UserCommandEntity();
    userCommandEntity.setId(userCommand.getId());
    userCommandEntity.setUserEmail(userEmail);
    userCommandEntity.setCommandName(userCommand.getCommandName());
    userCommandEntity.setCommandText(userCommand.getCommandText());
    userCommandEntity.setTimestamp(userCommand.getTimestamp());

    userCommandEntity.setFlags(userCommand.getFlags());
    userCommandEntity.setOptions(userCommand.getOptions());
    return userCommandEntity;
  }

  public PageResponse<UserCommand> mapToPageResponse(Page<UserCommandEntity> userCommandEntityPage, Filter filter) {
    PageResponse<UserCommand> pageResponse = new PageResponse<>();
    List<UserCommandEntity> userCommands = userCommandEntityPage.getContent();
    List<UserCommand> userCommandList = userCommands.stream().map(this::mapToUserCommand).collect(Collectors.toList());
    pageResponse.setRecords(userCommandList);
    pageResponse.setPageNumber(userCommandEntityPage.getNumber());
    pageResponse.setPageSize(filter.getPagination().getPageSize());
    pageResponse.setTotalSize(userCommandEntityPage.getTotalElements());
    return pageResponse;
  }
}
