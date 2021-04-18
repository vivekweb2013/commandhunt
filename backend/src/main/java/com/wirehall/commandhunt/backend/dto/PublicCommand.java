package com.wirehall.commandhunt.backend.dto;

public class PublicCommand extends BaseCommand {
  private boolean isDeletable = false;

  public boolean isDeletable() {
    return isDeletable;
  }

  public void setDeletable(boolean deletable) {
    isDeletable = deletable;
  }

  @Override
  public String toString() {
    return "PublicCommand{" + "isDeletable=" + isDeletable + "} " + super.toString();
  }
}
