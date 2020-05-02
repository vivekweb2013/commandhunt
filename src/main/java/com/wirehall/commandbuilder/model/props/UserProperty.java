package com.wirehall.commandbuilder.model.props;

public enum UserProperty {
  name(true),
  email(true),
  imageUrl(false),
  emailVerified(false),
  password(false),
  provider(true),
  providerId(false);

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
}
