package com.wirehall.commandhunt.backend.model;

import com.wirehall.commandhunt.backend.converter.OptionValueConverter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

@Entity
@Table(name = "usercommand")
public class UserCommandEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ElementCollection
  @MapKeyColumn(name = "name")
  @Column(name = "value")
  @CollectionTable(name = "usercommand_flag", joinColumns = @JoinColumn(name = "usercommand_id"))
  Map<String, Boolean> flags = new HashMap<>();

  @Convert(converter = OptionValueConverter.class, attributeName = "value")
  @ElementCollection
  @MapKeyColumn(name = "name")
  @Column(name = "value")
  @CollectionTable(name = "usercommand_option", joinColumns = @JoinColumn(name = "usercommand_id"))
  Map<String, List<String>> options = new HashMap<>();


  @Column(nullable = false, updatable = false)
  private String commandName;
  @Column(nullable = false)
  private String commandText;
  @Column(nullable = false, updatable = false)
  private String userEmail;
  @Column(nullable = false, updatable = false)
  private Timestamp createdOn;

  private Timestamp modifiedOn;
  @Column(nullable = false)
  private Timestamp operatedOn;

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

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
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

  public Map<String, Boolean> getFlags() {
    return flags;
  }

  public void setFlags(Map<String, Boolean> flags) {
    this.flags = flags;
  }

  public Map<String, List<String>> getOptions() {
    return options;
  }

  public void setOptions(Map<String, List<String>> options) {
    this.options = options;
  }

  @Override
  public String toString() {
    return "UserCommand{" +
        "id=" + id +
        ", commandName='" + commandName + '\'' +
        ", commandText='" + commandText + '\'' +
        ", userEmail='" + userEmail + '\'' +
        ", createdOn=" + createdOn +
        ", modifiedOn=" + modifiedOn +
        ", operatedOn=" + operatedOn +
        ", flags=" + flags +
        ", options=" + options +
        '}';
  }
}
