package com.wirehall.commandhunt.backend.controller;

import com.wirehall.commandhunt.backend.dto.MetaCommand;
import com.wirehall.commandhunt.backend.dto.filter.Filter;
import com.wirehall.commandhunt.backend.dto.filter.PageResponse;
import com.wirehall.commandhunt.backend.service.MetaCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "api", produces = MediaType.APPLICATION_JSON_VALUE)
public class MetaCommandController {

  private final MetaCommandService metaCommandService;

  @Autowired
  public MetaCommandController(MetaCommandService metaCommandService) {
    this.metaCommandService = metaCommandService;
  }

  @GetMapping(value = "/meta-command")
  public PageResponse<MetaCommand> getAllMetaCommands(@Valid Filter filter) {
    return metaCommandService.getAllMetaCommands(filter);
  }

  @GetMapping(value = "/meta-command/{id}")
  public MetaCommand getMetaCommandById(@PathVariable(name = "id") String id) {
    return metaCommandService.getMetaCommandById(id);
  }

  @GetMapping(value = "/meta-command/search")
  public MetaCommand getMetaCommandByName(@RequestParam(name = "name") String name) {
    return metaCommandService.getMetaCommandByName(name);
  }

  @GetMapping(
      value = "/meta-command/search",
      params = {"query"})
  public List<MetaCommand> getMatchingMetaCommands(@RequestParam(name = "query") String query) {
    return metaCommandService.getMatchingMetaCommands(query);
  }
}
