package com.wirehall.commandhunt.backend.controller;

import com.wirehall.commandhunt.backend.dto.PublicCommand;
import com.wirehall.commandhunt.backend.service.PublicCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

/**
 * Controller for performing action on public-command.
 * A public command can be retrieved by anyone without the need to login into the system.
 * Only logged-in user can create a public command.
 * A public command can not be updated after creation. Not even the owner of the command is allowed to update.
 * A public command can be deleted by the owner.
 */
@RestController
@RequestMapping(value = "api/public", produces = MediaType.APPLICATION_JSON_VALUE)
public class PublicCommandController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublicCommandController.class);

    private final PublicCommandService publicCommandService;

    @Autowired
    public PublicCommandController(PublicCommandService publicCommandService) {
        this.publicCommandService = publicCommandService;
    }

    @GetMapping(value = "/public-command/{id}")
    public Map<String, Object> getPublicCommandById(@PathVariable(name = "id") Long publicCommandId, Principal principal) {
        LOGGER.info("Request for retrieving public-command with id: {}", publicCommandId);
        String userEmail = principal == null ? null : principal.getName();
        return publicCommandService.getPublicCommandById(publicCommandId, userEmail);
    }

    @PostMapping(value = "/public-command")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void addPublicCommand(@RequestBody PublicCommand publicCommand, Principal principal) {
        LOGGER.info("Request from user: {} for adding public-command: {}", principal.getName(), publicCommand);
        publicCommandService.addPublicCommand(publicCommand, principal.getName());
    }

    @DeleteMapping(value = "/public-command/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteUserCommand(@PathVariable(name = "id") Long publicCommandId, Principal principal) {
        LOGGER.info("Request from user: {} for deleting public-command with id: {}", principal.getName(), publicCommandId);
        publicCommandService.deletePublicCommand(publicCommandId, principal.getName());
    }
}
