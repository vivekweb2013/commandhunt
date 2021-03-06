package com.wirehall.commandhunt.backend.model.graph.props;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FlagProperty {
  NAME("V", true),
  ALIAS("V", false),
  PREFIX("V", true),
  DESC("V", true),
  LONG_DESC("V", false),
  SEQUENCE("E", true),
  IS_GROUPING_ALLOWED("V", false),
  IS_SOLITARY("V", false);

  private final String propertyOf;
  private final boolean isMandatory;

  FlagProperty(String propertyOf, boolean isMandatory) {
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
