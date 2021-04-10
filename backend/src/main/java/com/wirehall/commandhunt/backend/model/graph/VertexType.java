package com.wirehall.commandhunt.backend.model.graph;

import com.fasterxml.jackson.annotation.JsonValue;

public enum VertexType {
  METACOMMAND,
  FLAG,
  OPTION,
  USER;

  @JsonValue
  public String toLowerCase() {
    return name().toLowerCase();
  }
}
