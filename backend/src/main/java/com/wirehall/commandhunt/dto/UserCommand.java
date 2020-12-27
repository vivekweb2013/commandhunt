package com.wirehall.commandhunt.dto;

import com.wirehall.commandhunt.model.props.UserCommandProperty;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class UserCommand extends Node<UserCommandProperty> {

  private Map<String, Object> flags = new HashMap<>();
  private Map<String, Object> options = new HashMap<>();

  public UserCommand() {
    super(UserCommandProperty.class);
  }

  /**
   * Retrieve the properties of user-command.
   *
   * @return The user-command properties.
   */
  @Override
  public Map<UserCommandProperty, Object> getProperties() {
    if (properties == null) {
      properties = new EnumMap<>(UserCommandProperty.class);
    }
    return properties;
  }

  /**
   * Returns the stored flags as name-value pairs.
   *
   * @return The key-value map of flag name and its value.
   */
  public Map<String, Object> getFlags() {
    return flags;
  }

  /**
   * Sets the flags as name-value pairs.
   *
   * @param flags The key-value map of flag name and its value.
   */
  public void setFlags(Map<String, Object> flags) {
    this.flags = flags;
  }

  /**
   * Returns the stored options as name-value pairs.
   *
   * @return The key-value map of option name and its value.
   */
  public Map<String, Object> getOptions() {
    return options;
  }

  /**
   * Sets the options as name-value pairs.
   *
   * @param options The key-value map of option name and its value.
   */
  public void setOptions(Map<String, Object> options) {
    this.options = options;
  }
}
