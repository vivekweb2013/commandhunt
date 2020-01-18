package com.wirehall.commandbuilder.dto;

import com.wirehall.commandbuilder.model.props.FLAG_PROPERTY;

import java.util.EnumMap;
import java.util.Map;

public class Flag extends Node<FLAG_PROPERTY> {

    public Flag() {
        super(FLAG_PROPERTY.class);
    }

    @Override
    public Map<FLAG_PROPERTY, Object> getProperties() {
        if (properties == null) {
            properties = new EnumMap<>(FLAG_PROPERTY.class);
        }
        return properties;
    }
}
