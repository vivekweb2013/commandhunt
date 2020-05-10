package com.wirehall.commandbuilder.dto.filter;

public class Sort {

  private String sortBy;

  private Direction sortOrder = Direction.ASC;

  public String getSortBy() {
    return sortBy;
  }

  public void setSortBy(String sortBy) {
    this.sortBy = sortBy;
  }

  public Direction getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(Direction sortOrder) {
    this.sortOrder = sortOrder;
  }

  @Override
  public String toString() {
    return "Sort{" + "sortBy='" + sortBy + '\'' + ", sortOrder=" + sortOrder + '}';
  }

  public enum Direction {
    ASC,
    DESC
  }

}
