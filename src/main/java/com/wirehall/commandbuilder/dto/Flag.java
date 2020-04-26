package com.wirehall.commandbuilder.dto;

import com.wirehall.commandbuilder.model.props.FlagProperty;
import java.util.EnumMap;
import java.util.Map;

public class Flag extends Node<FlagProperty> {

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
}
