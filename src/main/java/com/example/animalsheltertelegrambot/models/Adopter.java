package com.example.animalsheltertelegrambot.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;

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
    private String telephoneNumber;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "adopter")
    @JsonBackReference
    private Animal animal;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "adopter")
    @JoinColumn
    @JsonBackReference
    private ProbationPeriod probationPeriod = new ProbationPeriod();

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

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public Animal getAnimal() {
        return animal;
    }

    public String getUsername() {
        return username;
    }

    public ProbationPeriod getProbationPeriod() {
        return probationPeriod;
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

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public void setAnimals(Animal animal) {
        this.animal = animal;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public void setProbationPeriod(ProbationPeriod probationPeriod) {
        this.probationPeriod = probationPeriod;
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
