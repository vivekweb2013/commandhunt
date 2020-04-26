package com.wirehall.commandbuilder.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.Map;

public abstract class Node<E extends Enum<E>> {
  protected final Class<E> enumClass;
  protected Map<E, Object> properties;
  private Object id;

  public Node(Class<E> enumClass) {
    this.enumClass = enumClass;
  }

  public Object getId() {
    return id;
  }

  public void setId(Object id) {
    this.id = id;
  }

  public abstract Map<E, Object> getProperties();

  public void setProperties(Map<E, Object> properties) {
    this.properties = properties;
  }

  public Object getProperty(E key) {
    return getProperties().get(key);
  }

  public void addProperty(E key, Object value) {
    getProperties().put(key, value);
  }

  // The sole purpose of this overloaded private method is to be used for deserialization
  // Since @JsonAnySetter does not work with Enum key parameter
  @JsonAnySetter
  private void addProperty(String key, Object value) {
    addProperty(Enum.valueOf(enumClass, key), value);
  }

  @Override
  public String toString() {
    return "Node{" + "id=" + id + ", properties=" + properties + '}';
  }
}
