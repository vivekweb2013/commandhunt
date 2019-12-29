package com.wirehall.commandbuilder.model;

public enum COMMAND_PROPERTY {
    name("V"), desc("V"), long_desc("V");
    private final String propertyOf;

    COMMAND_PROPERTY(String propertyOf) {
        this.propertyOf = propertyOf;
    }

    /**
     * @return "V" if the property belongs to Vertex, "E" for Edge
     */
    public String propertyOf() {
        return propertyOf;
    }
}
