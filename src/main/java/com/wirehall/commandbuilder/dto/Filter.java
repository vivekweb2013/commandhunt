package com.wirehall.commandbuilder.dto;

public class Filter {
    public enum operator {EQUALS, CONTAINS, STARTS_WITH, ENDS_WITH, LESS_THAN, GREATER_THAN}

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
