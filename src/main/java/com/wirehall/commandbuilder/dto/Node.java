package com.wirehall.commandbuilder.dto;

import java.util.Map;

public abstract class Node<E extends Enum<E>> {
    private Object id;
    protected Map<E, Object> properties;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public abstract Map<E, Object> getProperties();

    public Object getProperty(E key) {
        return getProperties().get(key);
    }

    public void setProperties(Map<E, Object> properties) {
        this.properties = properties;
    }

    public void addProperty(E key, Object value) {
        getProperties().put(key, value);
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", properties=" + properties +
                '}';
    }
}
