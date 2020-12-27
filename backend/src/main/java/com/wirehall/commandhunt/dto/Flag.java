package com.wirehall.commandhunt.dto;

import com.wirehall.commandhunt.model.props.FlagProperty;
import com.wirehall.commandhunt.model.props.OptionProperty;
import java.util.EnumMap;
import java.util.Map;

public class Flag extends Node<FlagProperty> implements Comparable<Flag> {

  public Flag() {
    super(FlagProperty.class);
  }

  @Override
  public Map<FlagProperty, Object> getProperties() {
    if (properties == null) {
      properties = new EnumMap<>(FlagProperty.class);
    }
    return properties;
  }

  @Override
  public int compareTo(Flag o) {
    byte i = (byte) this.properties.get(FlagProperty.SEQUENCE);
    byte j = (byte) o.properties.get(FlagProperty.SEQUENCE);
    return i - j;
  }
}
