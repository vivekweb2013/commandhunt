package com.wirehall.commandhunt.backend.service;

import com.wirehall.commandhunt.backend.dto.MetaCommand;
import com.wirehall.commandhunt.backend.dto.filter.Filter;
import com.wirehall.commandhunt.backend.dto.filter.PageResponse;
import com.wirehall.commandhunt.backend.repository.MetaCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetaCommandService {

  private final MetaCommandRepository metaCommandRepository;

  @Autowired
  public MetaCommandService(MetaCommandRepository metaCommandRepository) {
    this.metaCommandRepository = metaCommandRepository;
  }

  public List<MetaCommand> getAllMetaCommands() {
    return metaCommandRepository.getAllMetaCommands();
  }

  public PageResponse<MetaCommand> getAllMetaCommands(Filter filter) {
    return metaCommandRepository.getAllMetaCommands(filter);
  }

  public MetaCommand getMetaCommandById(String id) {
    return metaCommandRepository.getMetaCommandById(id);
  }

  public MetaCommand getMetaCommandByName(String name) {
    return metaCommandRepository.getMetaCommandByName(name);
  }

  public List<MetaCommand> getMatchingMetaCommands(Filter filter) {
    return metaCommandRepository.getMatchingMetaCommands(filter);
  }

  public List<MetaCommand> getMatchingMetaCommands(String query) {
    return metaCommandRepository.getMatchingMetaCommands(query);
  }
}
