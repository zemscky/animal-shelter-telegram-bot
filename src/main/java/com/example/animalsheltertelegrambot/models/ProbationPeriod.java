package com.example.animalsheltertelegrambot.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;

@Entity
public class ProbationPeriod {

    @Id
    @GeneratedValue
    Long id;

    private LocalDate ends;

    @OneToOne
    private Animal animal;

    @ManyToOne
    private Adopter adopter;

    private HashMap<LocalDate, Report> reports;

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

    public HashMap<LocalDate, Report> getReports() {
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

    public void setReports(HashMap<LocalDate, Report> reports) {
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
