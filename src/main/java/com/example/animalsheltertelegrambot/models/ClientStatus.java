package com.example.animalsheltertelegrambot.models;

public enum ClientStatus {
    GUEST("Гость"), POTENTIAL_ADOPTIVE_PARENT("Потенциальный усыновитель"),
    ADOPTIVE_PARENT("Усыновитель");
    private final String status;
    ClientStatus(String status) {
        this.status = status;
    }
}
