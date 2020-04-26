package com.wirehall.commandbuilder.dto;

import com.wirehall.commandbuilder.model.props.OptionProperty;
import java.util.EnumMap;
import java.util.Map;

public class Option extends Node<OptionProperty> {

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
}
