package com.example.animalsheltertelegrambot.models;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

/**
 * this class stores the data of the user of the shelter
 */
@Entity
public class ShelterUser {
    /**
     * this field contains the chat ID of the ShelterUser and is the primary key of the shelter_user table in PostgreSQL
     */
    @Id
    Long chatId;
    /**
     * this field stores the status of the user in the telegram bot
     */
    UserStatus userStatus;
    /**
     * this field stores a variation of breeds that the user can choose when adopting an animal
     */
    ShelterType shelterType;
    /**
     * this field stores the user's phone number, which he specified in the telegram bot
     */
    String phoneNumber;
    /**
     * this field stores the nickname of the user that he specified in the telegram bot
     */
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
