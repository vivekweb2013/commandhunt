package com.wirehall.commandhunt.model.props;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserProperty {
  NAME(true),
  EMAIL(true),
  IMAGE_URL(false),
  EMAIL_VERIFIED(false),
  PASSWORD(false),
  PROVIDER(true),
  PROVIDER_ID(false);

  private final boolean isMandatory;

  UserProperty(boolean isMandatory) {
    this.isMandatory = isMandatory;
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
