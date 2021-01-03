package com.wirehall.commandhunt.backend.model.props;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CommandProperty {
  NAME("V", true),
  SYNTAX("V", true),
  DESC("V", true),
  LONG_DESC("V", false),
  MAN_PAGE_URL("V", false);

  private final String propertyOf;
  private final boolean isMandatory;

  CommandProperty(String propertyOf, boolean isMandatory) {
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
