package com.example.animalsheltertelegrambot.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Objects;
import java.util.Set;

@Entity
public class Client {
    /**
     * id (Client) - User identifier, primary key of the client table in database
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    /**
     * Client name
     */

    private Long chatId;

    private String name;
    /**
     * Client address
     */
    private String address;
    /**
     * Client age
     */
    private int age;
    /**
     * Client telephone number
     */
    private int telephoneNumber;
    /**
     * Client status (GUEST (гость),POTENTIAL_ADOPTIVE_PARENT (Потенциальный усыновитель),ADOPTIVE_PARENT(Усыновитель)
     * Method is used {@link ClientStatus}
     */
    private ClientStatus status;

    @OneToMany(mappedBy = "client")
    @JsonBackReference
    private Set<Animal> animals;

    public Client() {
    }

    public Client(long id, String name, String address, int age, int telephoneNumber, ClientStatus status) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.age = age;
        this.telephoneNumber = telephoneNumber;
        this.status = status;
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

    public ClientStatus getStatus() {
        return status;
    }

    public void setStatus(ClientStatus status) {
        this.status = status;
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
