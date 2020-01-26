package com.wirehall.commandbuilder.controller;

import com.wirehall.commandbuilder.dto.Command;
import com.wirehall.commandbuilder.dto.Filter;
import com.wirehall.commandbuilder.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api", produces = MediaType.APPLICATION_JSON_VALUE)
public class MainController {

    private final MainService mainService;

    @Autowired
    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping(value = "/command")
    public List<Command> getAllCommands() {
        return mainService.getAllCommands();
    }

    @GetMapping(value = "/command/{id}")
    public Command getCommandById(@PathVariable(name = "id") String id) {
        return mainService.getCommandById(id);
    }

    @GetMapping(value = "/command/search")
    public Command getCommandByName(@RequestParam(name = "name") String name) {
        return mainService.getCommandByName(name);
    }

    @GetMapping(value = "/command/search", params = {"query"})
    public List<Command> getMatchingCommands(@RequestParam(name = "query") String query) {
        return mainService.getMatchingCommands(query);
    }

    @PostMapping(value = "/command/search", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Command> getMatchingCommands(@RequestBody Filter filter) {
        return mainService.getMatchingCommands(filter);
    }
}
