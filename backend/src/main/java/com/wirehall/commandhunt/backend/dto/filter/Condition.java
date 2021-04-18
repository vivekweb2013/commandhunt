package com.wirehall.commandhunt.backend.dto.filter;

/**
 * This class is used to get the filter conditions for filtering entity based on the matching
 * criteria. The key belongs to a field in the entity, operator specifies the type of match. The
 * value will be matched against the field's value of the entity stored in database.
 */
public class Condition {

  private String key;
  private String value;
  private Operator operator;

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

  @Override
  public String toString() {
    return "Condition{"
        + "key='"
        + key
        + '\''
        + ", value='"
        + value
        + '\''
        + ", operator="
        + operator
        + '}';
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
