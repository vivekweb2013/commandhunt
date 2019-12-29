package com.wirehall.commandbuilder.model;

public enum FLAG_PROPERTY {
    name("V"), alias("V"), prefix("V"), desc("V"), long_desc("V"), sequence("E");

    private final String propertyOf;

    FLAG_PROPERTY(String propertyOf) {
        this.propertyOf = propertyOf;
    }

    /**
     * @return "V" if the property belongs to Vertex, "E" for Edge
     */
    public String propertyOf() {
        return propertyOf;
    }
}
