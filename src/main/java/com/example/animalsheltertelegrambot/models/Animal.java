package com.example.animalsheltertelegrambot.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.GenerationType;
import javax.persistence.*;
import java.util.Objects;

@Entity
public class Animal {
    /**
     * id (Animal) — animal identifier, primary key of animal table in database
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    /**
     * Animal name (nickname)
     */
    private String name;
    /**
     * Animal color
     */
    private String color;
    /**
     * Animal species (dog, cat, bird...)
     * Method is used {@link AnimalSpecies}
     */
    private AnimalSpecies species;
    private int age;
    private String uniqueCharacteristic;
    private String specialNeed;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = true)
    @JsonManagedReference
    private Client client;

    public Animal() {
    }

    public Animal(long id, String name, String color, AnimalSpecies species, int age) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.species = species;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public AnimalSpecies getSpecies() {
        return species;
    }

    public int getAge() {
        return age;
    }

    public String getUniqueCharacteristic() {
        return uniqueCharacteristic;
    }

    public String getSpecialNeed() {
        return specialNeed;
    }

    public Client getClient() {
        return client;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSpecies(AnimalSpecies species) {
        this.species = species;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setUniqueCharacteristic(String uniqueCharacteristic) {
        this.uniqueCharacteristic = uniqueCharacteristic;
    }

    public void setSpecialNeed(String specialNeed) {
        this.specialNeed = specialNeed;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return id == animal.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
