package com.wirehall.commandhunt.backend.dto;

import java.sql.Timestamp;

public class UserCommand extends BaseCommand {

  private Timestamp modifiedOn;
  private Timestamp operatedOn;

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

  @Override
  public String toString() {
    return "UserCommand{" +
            "modifiedOn=" + modifiedOn +
            ", operatedOn=" + operatedOn +
            "} " + super.toString();
  }
}
