package com.wirehall.commandbuilder.model.props;

public enum COMMAND_PROPERTY {
    name("V", true), syntax("V", true), desc("V", true),
    long_desc("V", false), man_page_url("V", false);

    private final String propertyOf;
    private final boolean isMandatory;

    COMMAND_PROPERTY(String propertyOf, boolean isMandatory) {
        this.propertyOf = propertyOf;
        this.isMandatory = isMandatory;
    }

    /**
     * @return "V" if the property belongs to Vertex, "E" for Edge
     */
    public String propertyOf() {
        return propertyOf;
    }

    /**
     * @return true if the property is mandatory, false otherwise
     */
    public boolean isMandatory() {
        return isMandatory;
    }
}
