package com.wirehall.commandbuilder.model.props;

public enum UserCommandProperty {
  userEmail("V", true),
  commandName("V", true),
  commandText("V", true),
  timestamp("V", true);

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
}
