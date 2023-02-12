package com.example.animalsheltertelegrambot.models;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class ShelterUser {
    @Id
    Long chatId;
    UserStatus userStatus;
    ShelterType shelterType;
    String phoneNumber;
    String username;

    public ShelterUser() {
    }

    public ShelterUser(Long chatId,
                       UserStatus userStatus,
                       ShelterType shelterType,
                       String phoneNumber,
                       String username) {
        this.chatId = chatId;
        this.userStatus = userStatus;
        this.shelterType = shelterType;
        this.phoneNumber = phoneNumber;
        this.username = username;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public ShelterType getShelterType() {
        return shelterType;
    }

    public void setShelterType(ShelterType shelterType) {
        this.shelterType = shelterType;
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
        ShelterUser user = (ShelterUser) o;
        return Objects.equals(chatId, user.chatId) && userStatus == user.userStatus && shelterType == user.shelterType && Objects.equals(phoneNumber, user.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, userStatus, shelterType, phoneNumber);
    }
}
