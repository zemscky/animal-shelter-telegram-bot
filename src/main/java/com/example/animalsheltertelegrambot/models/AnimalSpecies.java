package com.example.animalsheltertelegrambot.models;

public enum AnimalSpecies {
    DOG("Собака"), CAT("Кошка"), BIRD("Птица");
    private final String species;

    AnimalSpecies(String type) {
        this.species = type;
    }
}

