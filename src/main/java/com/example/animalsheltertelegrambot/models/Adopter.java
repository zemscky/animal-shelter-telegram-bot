package com.example.animalsheltertelegrambot.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class Adopter {

    @Id
    @GeneratedValue
    private long id;

    private Long chatId;
    private String username;
    private String name;
    private String address;
    private int age;
    private int telephoneNumber;

    @OneToOne(mappedBy = "adopter")
    @JsonBackReference
    private Animal animal;

    @OneToOne(mappedBy = "adopter")
    @JsonBackReference
    private ProbationPeriod probationPeriod;

    public Adopter() {
    }

    public long getId() {
        return id;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getAge() {
        return age;
    }

    public int getTelephoneNumber() {
        return telephoneNumber;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setTelephoneNumber(int telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public void setAnimals(Animal animal) {
        this.animal = animal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adopter adopter = (Adopter) o;
        return id == adopter.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
