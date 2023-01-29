package com.example.animalsheltertelegrambot.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ShelterMessage {
    @Id
    @GeneratedValue
    private long id;

    private String description;
    private String messageText;

    public ShelterMessage() {
    }

    public ShelterMessage(long id, String description, String text) {
        this.id = id;
        this.description = description;
        this.messageText = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String text) {
        this.messageText = text;
    }
}
