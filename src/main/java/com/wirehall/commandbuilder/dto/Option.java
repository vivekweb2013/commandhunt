package com.wirehall.commandbuilder.dto;

import com.wirehall.commandbuilder.model.props.OPTION_PROPERTY;

import java.util.EnumMap;
import java.util.Map;

public class Option extends Node<OPTION_PROPERTY> {

  public Option() {
    super(OPTION_PROPERTY.class);
  }

  @Override
  public Map<OPTION_PROPERTY, Object> getProperties() {
    if (properties == null) {
      properties = new EnumMap<>(OPTION_PROPERTY.class);
    }
    return properties;
  }
}
