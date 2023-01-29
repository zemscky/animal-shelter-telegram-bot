package com.example.animalsheltertelegrambot.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Animal {

    @Id
    @GeneratedValue
    private long id;

    private String name;
    private String color;
    private String animalType;
    private int age;

    public Animal() {
    }

    public Animal(long id, String name, String color, String animalType, int age) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.animalType = animalType;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
