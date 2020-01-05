package com.wirehall.commandbuilder.model.props;

public enum OPTION_PROPERTY {
    name("V", true), alias("V", false),
    prefix("V", false), desc("V", true),
    long_desc("V", false), data_type("V", true),
    is_mandatory("E", true), is_repeatable("E", true),
    sequence("E", true);

    private final String propertyOf;
    private final boolean isMandatory;

    OPTION_PROPERTY(String propertyOf, boolean isMandatory) {
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
