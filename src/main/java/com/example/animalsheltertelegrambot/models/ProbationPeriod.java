package com.example.animalsheltertelegrambot.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class ProbationPeriod {

    @Id
    @GeneratedValue
    Long id;

    private LocalDate ends;
    private boolean wasSuccessful;
    private String volunteersComment;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    @JsonBackReference
    private Animal animal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    @JsonBackReference
    private Adopter adopter;

    @OneToMany(fetch = FetchType.EAGER)
    @JsonBackReference
    private List<Report> reports;

    public ProbationPeriod() {
    }

    public Long getId() {
        return id;
    }

    public LocalDate getEnds() {
        return ends;
    }

    public Animal getAnimal() {
        return animal;
    }

    public Adopter getAdopter() {
        return adopter;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEnds(LocalDate ends) {
        this.ends = ends;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public void setAdopter(Adopter adopter) {
        this.adopter = adopter;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProbationPeriod that = (ProbationPeriod) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
