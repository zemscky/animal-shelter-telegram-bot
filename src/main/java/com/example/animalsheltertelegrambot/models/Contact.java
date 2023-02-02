package com.example.animalsheltertelegrambot.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Contact {
    @Id
    Long chatId;
    String phoneNumber;

    public Contact() {
    }

    public Contact(Long chatId, String phoneNumber) {
        this.chatId = chatId;
        this.phoneNumber = phoneNumber;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(chatId, contact.chatId) && Objects.equals(phoneNumber, contact.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, phoneNumber);
    }
}
