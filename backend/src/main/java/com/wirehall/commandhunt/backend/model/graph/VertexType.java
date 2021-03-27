package com.wirehall.commandhunt.backend.model.graph;

import com.fasterxml.jackson.annotation.JsonValue;

public enum VertexType {
  COMMAND,
  USERCOMMAND,
  FLAG,
  FLAGVALUE,
  OPTION,
  OPTIONVALUE,
  USER;

  @JsonValue
  public String toLowerCase() {
    return name().toLowerCase();
  }
}
