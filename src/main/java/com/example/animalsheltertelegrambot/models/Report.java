package com.example.animalsheltertelegrambot.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "probation_period_id", nullable = false)
    @JsonManagedReference
    private ProbationPeriod probationPeriod;

    private LocalDate date;
    private String entry;
    private String photoId;

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

    public String getPhotoId() {
        return photoId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
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
