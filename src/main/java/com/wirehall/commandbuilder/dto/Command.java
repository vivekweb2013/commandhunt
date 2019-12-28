package com.wirehall.commandbuilder.dto;

import java.util.HashMap;
import java.util.Map;

public class Command {
    private String id;
    private String label;
    private Map<String, Object> properties;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    @Override
    public String toString() {
        return "Command{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", properties=" + properties +
                '}';
    }
}
