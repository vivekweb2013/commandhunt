package com.wirehall.commandbuilder.model.props;

public enum OptionProperty {
  name("V", true),
  alias("V", false),
  prefix("V", false),
  desc("V", true),
  long_desc("V", false),
  data_type("V", true),
  is_mandatory("E", true),
  is_repeatable("E", true),
  sequence("E", true);

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
}
