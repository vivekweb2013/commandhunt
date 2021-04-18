package com.wirehall.commandhunt.backend.dto;

import com.wirehall.commandhunt.backend.model.graph.props.MetaCommandProperty;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** This class holds the metadata of a specific command. */
public class MetaCommand extends Node<MetaCommandProperty> {

  private List<Flag> flags;
  private List<Option> options;

  public MetaCommand() {
    super(MetaCommandProperty.class);
  }

  /**
   * Retrieve the properties of command.
   *
   * @return The command properties.
   */
  @Override
  public Map<MetaCommandProperty, Object> getProperties() {
    if (properties == null) {
      properties = new EnumMap<>(MetaCommandProperty.class);
    }
    return properties;
  }

  /**
   * Retrieve the flags of command.
   *
   * @return The list of command flags,
   */
  public List<Flag> getFlags() {
    if (flags == null) {
      flags = new ArrayList<>();
    }
    return flags;
  }

  public void setFlags(List<Flag> flags) {
    this.flags = flags;
  }

  public void addFlag(Flag flag) {
    getFlags().add(flag);
  }

  /**
   * Retrieve the option of command.
   *
   * @return The list of command options.
   */
  public List<Option> getOptions() {
    if (options == null) {
      options = new ArrayList<>();
    }
    return options;
  }

  public void setOptions(List<Option> options) {
    this.options = options;
  }

  public void addOption(Option option) {
    getOptions().add(option);
  }

  @Override
  public String toString() {
    return "MetaCommand{"
        + "flags="
        + flags
        + ", options="
        + options
        + ", properties="
        + properties
        + '}';
  }
}
