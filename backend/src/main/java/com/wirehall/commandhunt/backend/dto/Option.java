package com.wirehall.commandhunt.backend.dto;

import com.wirehall.commandhunt.backend.model.props.OptionProperty;
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
  public int compareTo(Option o) {
    byte i = (byte) this.properties.get(OptionProperty.SEQUENCE);
    byte j = (byte) o.properties.get(OptionProperty.SEQUENCE);
    return i - j;
  }
}
