package com.wirehall.commandhunt.backend.dto;

import com.wirehall.commandhunt.backend.model.props.UserProperty;
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
