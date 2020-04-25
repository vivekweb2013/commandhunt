package com.wirehall.commandbuilder.model.props;

public enum USER_PROPERTY {
    name(true), email(true), imageUrl(false), emailVerified(true),
    password(true), provider(true), providerId(false);

    private final boolean isMandatory;

    USER_PROPERTY(boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    /**
     * @return true if the property is mandatory, false otherwise
     */
    public boolean isMandatory() {
        return isMandatory;
    }
}
