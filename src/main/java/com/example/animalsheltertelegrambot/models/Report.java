package com.example.animalsheltertelegrambot.models;

import com.pengrad.telegrambot.model.File;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class Report {

    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    private ProbationPeriod probationPeriod;

    private String entry;
    private File photo;

    public Report() {
    }

    public Long getId() {
        return id;
    }

    public ProbationPeriod getProbationPeriod() {
        return probationPeriod;
    }

    public String getEntry() {
        return entry;
    }

    public File getPhoto() {
        return photo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProbationPeriod(ProbationPeriod probationPeriod) {
        this.probationPeriod = probationPeriod;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public void setPhoto(File photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(id, report.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
