package com.wirehall.commandbuilder.dto;

import com.wirehall.commandbuilder.model.props.COMMAND_PROPERTY;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Command extends Node<COMMAND_PROPERTY> {
    private List<Flag> flags;
    private List<Option> options;

    @Override
    public Map<COMMAND_PROPERTY, Object> getProperties() {
        if (properties == null) {
            properties = new EnumMap<>(COMMAND_PROPERTY.class);
        }
        return properties;
    }

    public List<Flag> getFlags() {
        if (flags == null) {
            flags = new ArrayList<>();
        }
        return flags;
    }

    public void addFlag(Flag flag) {
        getFlags().add(flag);
    }

    public void setFlags(List<Flag> flags) {
        this.flags = flags;
    }

    public List<Option> getOptions() {
        if (options == null) {
            options = new ArrayList<>();
        }
        return options;
    }

    public void addOption(Option option) {
        getOptions().add(option);
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "Command{" +
                "flags=" + flags +
                ", options=" + options +
                ", properties=" + properties +
                '}';
    }
}
