package com.wirehall.commandhunt.model.props;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OptionProperty {
  NAME("V", true),
  ALIAS("V", false),
  PREFIX("V", false),
  DESC("V", true),
  LONG_DESC("V", false),
  DATA_TYPE("V", true),
  IS_MANDATORY("E", true),
  IS_REPEATABLE("E", true),
  SEQUENCE("E", true);

  private final String propertyOf;
  private final boolean isMandatory;

  OptionProperty(String propertyOf, boolean isMandatory) {
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
   * Used to check if the property is mandatory or optional.
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
