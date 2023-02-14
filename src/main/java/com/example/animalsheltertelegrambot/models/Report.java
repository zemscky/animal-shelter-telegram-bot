package com.example.animalsheltertelegrambot.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Report {
    /**
     * this field contains the ID of the Report and is the primary key of the report table in PostgreSQL
     */
    @Id
    @GeneratedValue
    Long id;
    /**
     * this field stores the probationary period during which the adopter must send a report about the animal
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    @JsonBackReference
    private ProbationPeriod probationPeriod;
    /**
     * this field contains the date of the report sent by the adopter
     */
    private LocalDate date;
    /**
     * this field stores the report record of the adopter
     */
    private String entry;
    /**
     * this field stores the id of the animal's photo
     */
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
