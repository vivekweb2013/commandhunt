package com.wirehall.commandhunt.backend.dto;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserCommand {

  private Long id;

  @NotNull
  private String commandName;
  @NotNull
  private String commandText;

  private Timestamp createdOn;
  private Timestamp modifiedOn;
  private Timestamp operatedOn;

  private Map<String, Boolean> flags = new HashMap<>();
  private Map<String, List<String>> options = new HashMap<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCommandName() {
    return commandName;
  }

  public void setCommandName(String commandName) {
    this.commandName = commandName;
  }

  public String getCommandText() {
    return commandText;
  }

  public void setCommandText(String commandText) {
    this.commandText = commandText;
  }

  public Timestamp getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Timestamp createdOn) {
    this.createdOn = createdOn;
  }

  public Timestamp getModifiedOn() {
    return modifiedOn;
  }

  public void setModifiedOn(Timestamp modifiedOn) {
    this.modifiedOn = modifiedOn;
  }

  public Timestamp getOperatedOn() {
    return operatedOn;
  }

  public void setOperatedOn(Timestamp operatedOn) {
    this.operatedOn = operatedOn;
  }

  /**
   * Returns the stored flags as name-value pairs.
   *
   * @return The key-value map of flag name and its value.
   */
  public Map<String, Boolean> getFlags() {
    return flags;
  }

  /**
   * Sets the flags as name-value pairs.
   *
   * @param flags The key-value map of flag name and its value.
   */
  public void setFlags(Map<String, Boolean> flags) {
    this.flags = flags;
  }

  /**
   * Returns the stored options as name-value pairs.
   *
   * @return The key-value map of option name and its value.
   */
  public Map<String, List<String>> getOptions() {
    return options;
  }

  /**
   * Sets the options as name-value pairs.
   *
   * @param options The key-value map of option name and its value.
   */
  public void setOptions(Map<String, List<String>> options) {
    this.options = options;
  }

  @Override
  public String toString() {
    return "UserCommand{" +
            "id=" + id +
            ", commandName='" + commandName + '\'' +
            ", commandText='" + commandText + '\'' +
            ", createdOn=" + createdOn +
            ", modifiedOn=" + modifiedOn +
            ", operatedOn=" + operatedOn +
            ", flags=" + flags +
            ", options=" + options +
            '}';
  }
}
