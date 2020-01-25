package com.wirehall.commandbuilder.service;

import com.wirehall.commandbuilder.dto.Command;
import com.wirehall.commandbuilder.dto.Filter;
import com.wirehall.commandbuilder.repository.MainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainService {

    private MainRepository mainRepository;

    @Autowired
    public MainService(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    public List<Command> getAllCommands() {
        return mainRepository.getAllCommands();
    }

    public Command getCommandById(String id) {
        return mainRepository.getCommandById(id);
    }

    public Command getCommandByName(String name) {
        return mainRepository.getCommandByName(name);
    }

    public List<Command> getMatchingCommands(Filter filter) {
        return mainRepository.getMatchingCommands(filter);
    }

    public List<Command> getMatchingCommands(String query) {
        return mainRepository.getMatchingCommands(query);
    }
}
