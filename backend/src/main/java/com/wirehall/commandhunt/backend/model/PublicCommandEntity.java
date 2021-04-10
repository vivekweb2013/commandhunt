package com.wirehall.commandhunt.backend.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "public_command")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class PublicCommandEntity {
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    Set<String> flags = new HashSet<>();
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    Map<String, List<String>> options = new HashMap<>();
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, updatable = false)
    private String commandName;
    @Column(nullable = false)
    private String commandText;
    @Column(nullable = false, updatable = false)
    private String userEmail;
    @Column(nullable = false, updatable = false)
    private Timestamp createdOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<String> getFlags() {
        return flags;
    }

    public void setFlags(Set<String> flags) {
        this.flags = flags;
    }

    public Map<String, List<String>> getOptions() {
        return options;
    }

    public void setOptions(Map<String, List<String>> options) {
        this.options = options;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandText() {
        return commandText;
    }

    public void setCommandText(String commandText) {
        this.commandText = commandText;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "PublicCommandEntity{" +
                "id=" + id +
                ", flags=" + flags +
                ", options=" + options +
                ", commandName='" + commandName + '\'' +
                ", commandText='" + commandText + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", createdOn=" + createdOn +
                '}';
    }
}
