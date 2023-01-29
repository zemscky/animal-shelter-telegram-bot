package com.example.animalsheltertelegrambot.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ShelterMessage {
    @Id
    @GeneratedValue
    private String tag;

    private String messageText;

    public ShelterMessage() {
    }

    public ShelterMessage(String tag, String text) {
        this.tag = tag;
        this.messageText = text;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String text) {
        this.messageText = text;
    }
}
