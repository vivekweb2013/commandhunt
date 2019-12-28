package com.wirehall.commandbuilder.controller;

import com.wirehall.commandbuilder.dto.Command;
import com.wirehall.commandbuilder.dto.Filter;
import com.wirehall.commandbuilder.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "command", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class MainController {

    private final MainService mainService;

    @Autowired
    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping(value = "/{id}")
    public Command getCommandById(@PathVariable String id) {
        return mainService.getCommandById(id);
    }

    @GetMapping(value = "/")
    public List<Command> getAllCommands() {
        return mainService.getAllCommands();
    }

    @GetMapping(value = "/search")
    public Command getCommandByName(@RequestParam String name) {
        return mainService.getCommandByName(name);
    }

    @PostMapping(value = "/search")
    public List<Command> getCommandsByFilter(@RequestBody Filter filter) {
        return mainService.getCommandsByFilter(filter);
    }
}
