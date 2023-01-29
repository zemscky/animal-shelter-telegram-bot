package com.example.animalsheltertelegrambot.models;

public enum AnimalType {
    DOG("Собака"), CAT("Кошка"), BIRD("Птица");
    private final String type;

    AnimalType(String type) {
        this.type = type;
    }
}
