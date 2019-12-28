package com.wirehall.commandbuilder.dto;

public class Filter {
    public enum OPERATOR {EQUALS, CONTAINS, STARTS_WITH, ENDS_WITH, LESS_THAN, GREATER_THAN}

    private String key;
    private String value;
    private OPERATOR operator;

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

    public OPERATOR getOperator() {
        return operator;
    }

    public void setOperator(OPERATOR operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", operator=" + operator +
                '}';
    }
}
