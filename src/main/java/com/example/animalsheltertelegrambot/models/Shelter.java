package com.example.animalsheltertelegrambot.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Shelter {
    /**
     * this field contains the number of the Shelter and is the primary key of the shelter table in PostgreSQL
     */
    @Id
    private String number;
    /**
     * this field contains the name of the shelter, which will be shown to the user as information about the shelter
     */
    private String name;
    /**
     * this field contains the address of the shelter, which will be shown to the user as information about the shelter
     */
    private String address;
    /**
     * this field stores the phone number of the shelter, which will be shown to the user as information about the shelter
     */
    private String telephoneNumber;
    /**
     * this field stores the working hours of the shelter, which will be shown to the user in the form of information about the shelter
     */
    private String timetable;
    /**
     * this field stores the type of shelter
     */
    private ShelterType shelterType;

    @OneToMany(mappedBy = "shelter")
    @JsonManagedReference
    private Set<Animal> animals;

    public Shelter() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getTimetable() {
        return timetable;
    }

    public void setTimetable(String timetable) {
        this.timetable = timetable;
    }

    public ShelterType getShelterType() {
        return shelterType;
    }

    public void setShelterType(ShelterType shelterType) {
        this.shelterType = shelterType;
    }

    public Set<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(Set<Animal> animals) {
        this.animals = animals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelter shelter = (Shelter) o;
        return Objects.equals(number, shelter.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
