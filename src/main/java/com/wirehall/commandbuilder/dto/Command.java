package com.wirehall.commandbuilder.dto;

import java.util.List;

public class Command {
    private String id;
    private List<Object> properties;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
