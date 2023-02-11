package com.example.animalsheltertelegrambot.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class Client {

    @Id
    @GeneratedValue
    private long id;

    private Long chatId;

    private String name;
    private String address;
    private int age;
    private int telephoneNumber;

    @OneToMany(mappedBy = "client")
    @JsonBackReference
    private Set<Animal> animals;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shelter_number", nullable = false)
    @JsonBackReference
    private Shelter shelter;

    public Client() {
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

    public Set<Animal> getAnimals() {
        return animals;
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

    public void setAnimals(Set<Animal> animals) {
        this.animals = animals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
