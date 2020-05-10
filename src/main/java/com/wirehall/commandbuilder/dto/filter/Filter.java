package com.wirehall.commandbuilder.dto.filter;

import javax.validation.constraints.NotNull;

public class Filter {

  private String key;
  private String value;
  private Operator operator;

  @NotNull
  private Pageable pageable;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public Operator getOperator() {
    return operator;
  }

  public void setOperator(Operator operator) {
    this.operator = operator;
  }

  public Pageable getPageable() {
    return pageable;
  }

  public void setPageable(Pageable pageable) {
    this.pageable = pageable;
  }

  @Override
  public String toString() {
    return "Filter{" + "key='" + key + '\'' + ", value='" + value + '\''
        + ", operator=" + operator + ", pageable=" + pageable + '}';
  }

  public enum Operator {
    EQUALS,
    CONTAINS,
    STARTS_WITH,
    ENDS_WITH,
    LESS_THAN,
    GREATER_THAN
  }
}
