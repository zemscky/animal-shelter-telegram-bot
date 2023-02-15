package com.example.animalsheltertelegrambot.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String color;
    private String species;
    private int age;
    private String uniqueCharacteristics;
    private String specialNeeds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopter_id")
    @JsonManagedReference
    private Adopter adopter;

    @ManyToOne(fetch = FetchType.LAZY)                 //с FetchType.LAZY при получении приюта животного ошибка 500
    @JoinColumn(name = "shelter_id", nullable = false)                     // (name = "shelter_id", nullable = false)
    @JsonManagedReference
    private Shelter shelter;

    @OneToOne(mappedBy = "animal", cascade = CascadeType.ALL)    // mappedBy = "animal",
//    @JoinColumn(name = "probation_period_id")
    @JsonBackReference
    private ProbationPeriod probationPeriod;

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

    public String getUniqueCharacteristics() {
        return uniqueCharacteristics;
    }

    public String getSpecialNeeds() {
        return specialNeeds;
    }

    public Adopter getClient() {
        return adopter;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public Adopter getAdopter() {
        return adopter;
    }

    public void setAdopter(Adopter adopter) {
        this.adopter = adopter;
    }

    public ProbationPeriod getProbationPeriod() {
        return probationPeriod;
    }

    public void setProbationPeriod(ProbationPeriod probationPeriod) {
        this.probationPeriod = probationPeriod;
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

    public void setUniqueCharacteristics(String uniqueCharacteristics) {
        this.uniqueCharacteristics = uniqueCharacteristics;
    }

    public void setSpecialNeeds(String specialNeeds) {
        this.specialNeeds = specialNeeds;
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
