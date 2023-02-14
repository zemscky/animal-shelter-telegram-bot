package com.example.animalsheltertelegrambot.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;

/**
 * The adopter class contains information about the data of the adopter, which are entered into the database
 */
@Entity
public class Adopter {
    /**
     * this field contains the ID of the Adopter and is the primary key of the adaptation table in PostgreSQL
     */
    @Id
    @GeneratedValue
    private long id;
    /**
     * this field contains the ID of the adopter's chat to which messages about the adoption of the animal are sent
     */
    private Long chatId;
    /**
     * this field contains the nickname of the adopter required for reports
     */
    private String username;
    /**
     * this field contains the name of the adopter, which he indicated during registration
     */
    private String name;
    /**
     * this field contains the address of the adopter, which he indicated during registration
     */
    private String address;
    /**
     * this field contains the age of the adopter, which he indicated during registration
     */
    private int age;
    /**
     * this field contains the telephone number of the adopter, which he indicated during registration
     */
    private String telephoneNumber;
    /**
     * the animal field in the adopter class contains the relation One-To-One
     */
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "adopter")
    @JsonBackReference
    private Animal animal;
    /**
     * the probation field in the adopter class contains a One-To-One relationship and is required to add a probation for the adopter
     */
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
