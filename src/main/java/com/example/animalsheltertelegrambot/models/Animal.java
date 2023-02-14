package com.example.animalsheltertelegrambot.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Animal {
    /**
     * this field contains the ID of the animal and is the primary key of the animal table in PostgreSQL
     */
    @Id
    @GeneratedValue
    private long id;
    /**
     * this field contains the nickname of the animal
     */
    private String name;
    /**
     * this field contains the color of the animal
     */
    private String color;
    /**
     * this field contains the species of the animal
     */
    private String species;
    /**
     * this field contains the age of the animal
     */
    private int age;
    /**
     * this field contains the unique characteristic of the animal
     */
    private String uniqueCharacteristic;
    /**
     * this field contains the special need of the animal
     */
    private String specialNeed;
    /**
     * the adopter field in the animal class contains the relation One-To-One
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "adopter_id", nullable = true)
    @JsonManagedReference
    private Adopter adopter;
    /**
     * the shelter field in the animal class contains the relation One-To-One
     */
    @ManyToOne(fetch = FetchType.EAGER)                 //с FetchType.LAZY при получении приюта животного ошибка 500
    @JoinColumn(name = "shelter_id", nullable = false)
    @JsonBackReference
    private Shelter shelter;

    public Animal() {
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

    public String getSpecies() {
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

    public Adopter getClient() {
        return adopter;
    }

    public Shelter getShelter() {
        return shelter;
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

    public void setSpecies(String species) {
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

    public void setClient(Adopter adopter) {
        this.adopter = adopter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
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
