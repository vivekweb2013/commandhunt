package com.wirehall.commandhunt.backend.mapper;

import com.wirehall.commandhunt.backend.dto.PublicCommand;
import com.wirehall.commandhunt.backend.model.PublicCommandEntity;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PublicCommandMapper {

    /**
     * Maps public-command entity to public-command dto
     *
     * @param publicCommandEntity To be mapped to public-command dto.
     * @param mapAssociations     This flag should be set to false if entity's associations are not required.
     *                            Since un-necessarily mapping this will invoke lazy loading of these associations.
     * @return The public-command dto.
     */
    public PublicCommand mapToPublicCommand(PublicCommandEntity publicCommandEntity, boolean mapAssociations) {
        PublicCommand publicCommand = new PublicCommand();
        publicCommand.setId(publicCommandEntity.getId());

        publicCommand.setCommandName(publicCommandEntity.getCommandName());
        publicCommand.setCommandText(publicCommandEntity.getCommandText());
        publicCommand.setCreatedOn(publicCommandEntity.getCreatedOn());

        if (mapAssociations) {
            publicCommand.setFlags(publicCommandEntity.getFlags().stream().collect(Collectors.toMap(f -> f, f -> true)));
            publicCommand.setOptions(publicCommandEntity.getOptions());
        }
        return publicCommand;
    }

    /**
     * Maps public-command dto to public-command entity
     *
     * @param publicCommand The public-command dto.
     * @param userEmail     Logged in user's email-id.
     * @return The public-command entity.
     */
    public PublicCommandEntity mapToPublicCommandEntity(PublicCommand publicCommand, String userEmail) {
        PublicCommandEntity publicCommandEntity = new PublicCommandEntity();
        publicCommandEntity.setId(publicCommand.getId());
        publicCommandEntity.setUserEmail(userEmail);
        publicCommandEntity.setCommandName(publicCommand.getCommandName());
        publicCommandEntity.setCommandText(publicCommand.getCommandText());
        publicCommandEntity.setCreatedOn(publicCommand.getCreatedOn());

        Set<String> flags = publicCommand.getFlags().entrySet().stream().filter(Map.Entry::getValue)
                .map(Map.Entry::getKey).collect(Collectors.toSet());
        publicCommandEntity.setFlags(flags);
        publicCommandEntity.setOptions(publicCommand.getOptions());
        return publicCommandEntity;
    }
}
