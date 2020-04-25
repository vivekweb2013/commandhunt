package com.wirehall.commandbuilder.dto;

import com.wirehall.commandbuilder.model.props.USER_PROPERTY;

import java.util.EnumMap;
import java.util.Map;


public class User extends Node<USER_PROPERTY> {
    public User() {
        super(USER_PROPERTY.class);
    }

    @Override
    public Map<USER_PROPERTY, Object> getProperties() {
        if (properties == null) {
            properties = new EnumMap<>(USER_PROPERTY.class);
        }
        return properties;
    }

    public enum OAUTH_PROVIDER {
        local,
        facebook,
        google,
        github
    }
}


