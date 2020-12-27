package com.wirehall.commandhunt.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EdgeType {
  BELONGS_TO,
  HAS_FLAG,
  HAS_FLAG_VALUE,
  HAS_OPTION,
  HAS_OPTION_VALUE,
  OVERRIDES;

  @JsonValue
  public String toLowerCase() {
    return name().toLowerCase();
  }
}
