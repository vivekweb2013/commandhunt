package com.wirehall.commandbuilder.dto;

import java.util.HashMap;
import java.util.Map;

public class Flag {
    private Object id;
    private Map<String, Object> properties;

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

    @Override
    public String toString() {
        return "Flag{" +
                "id=" + id +
                ", properties=" + properties +
                '}';
    }
}
