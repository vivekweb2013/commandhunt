package com.wirehall.commandbuilder.model;

public enum OPTION_PROPERTY {
    name("V"), alias("V"), prefix("V"), desc("V"), long_desc("V"),
    data_type("V"), is_mandatory("E"), is_repeatable("E"), sequence("E");

    private final String propertyOf;

    OPTION_PROPERTY(String propertyOf) {
        this.propertyOf = propertyOf;
    }

    /**
     * @return "V" if the property belongs to Vertex, "E" for Edge
     */
    public String propertyOf() {
        return propertyOf;
    }
}
