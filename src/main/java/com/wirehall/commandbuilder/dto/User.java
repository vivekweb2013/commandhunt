package com.wirehall.commandbuilder.dto;

import com.wirehall.commandbuilder.model.props.UserProperty;
import java.util.EnumMap;
import java.util.Map;

public class User extends Node<UserProperty> {

  public User() {
    super(UserProperty.class);
  }

  @Override
  public Map<UserProperty, Object> getProperties() {
    if (properties == null) {
      properties = new EnumMap<>(UserProperty.class);
    }
    return properties;
  }

  public enum OAuthProvider {
    local,
    facebook,
    google,
    github
  }
}
