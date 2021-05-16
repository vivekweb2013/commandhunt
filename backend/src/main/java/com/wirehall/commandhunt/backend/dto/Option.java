package com.wirehall.commandhunt.backend.dto;

import com.wirehall.commandhunt.backend.model.graph.props.OptionProperty;
import org.springframework.lang.NonNull;

import java.util.EnumMap;
import java.util.Map;

public class Option extends Node<OptionProperty> implements Comparable<Option> {

  public Option() {
    super(OptionProperty.class);
  }

  @Override
  public Map<OptionProperty, Object> getProperties() {
    if (properties == null) {
      properties = new EnumMap<>(OptionProperty.class);
    }
    return properties;
  }

  @Override
  public int compareTo(@NonNull Option o) {
    if (this.equals(o)) return 0;
    byte i = (byte) this.properties.get(OptionProperty.SEQUENCE);
    byte j = (byte) o.properties.get(OptionProperty.SEQUENCE);
    return i - j;
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
