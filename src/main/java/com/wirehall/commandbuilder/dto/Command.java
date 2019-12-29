package com.wirehall.commandbuilder.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Command {
    private Object id;
    private Map<String, Object> properties;
    private List<Flag> flags;
    private List<Option> options;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Map<String, Object> getProperties() {
        if (properties == null) {
            properties = new HashMap<>();
        }
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public void addProperty(String key, Object value) {
        getProperties().put(key, value);
    }

    public List<Flag> getFlags() {
        return flags;
    }

    public void setFlags(List<Flag> flags) {
        this.flags = flags;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "Command{" +
                "id=" + id +
                ", properties=" + properties +
                ", flags=" + flags +
                ", options=" + options +
                '}';
    }
}
