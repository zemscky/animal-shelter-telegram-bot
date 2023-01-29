package com.example.animalsheltertelegrambot.models;

public enum MessageId {
    GREETINGS(1L),
    SHELTER_DESCRIPTION(2L),
    CALLBACK(3);

    private long id;

    MessageId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
