package com.example.animalsheltertelegrambot.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
