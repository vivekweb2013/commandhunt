package com.wirehall.commandbuilder.model.props;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserCommandProperty {
  USER_EMAIL("V", true),
  COMMAND_NAME("V", true),
  COMMAND_TEXT("V", true),
  TIMESTAMP("V", true);

  private final String propertyOf;
  private final boolean isMandatory;

  UserCommandProperty(String propertyOf, boolean isMandatory) {
    this.propertyOf = propertyOf;
    this.isMandatory = isMandatory;
  }

  /**
   * Used to identify if the property belongs to vertex or edge.
   *
   * @return "V" if the property belongs to Vertex, "E" for Edge.
   */
  public String propertyOf() {
    return propertyOf;
  }

  /**
   * Used to identify if the property is mandatory or optional.
   *
   * @return true if the property is mandatory, false otherwise.
   */
  public boolean isMandatory() {
    return isMandatory;
  }

  @JsonValue
  public String toLowerCase() {
    return name().toLowerCase();
  }
}
