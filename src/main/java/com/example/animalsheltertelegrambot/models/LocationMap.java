package com.example.animalsheltertelegrambot.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class LocationMap {
    /**
     * this field contains the number of the LocationMap and
     * is the primary key of the location_map table in PostgreSQL
     */
    @Id
    private String number;
    /**
     * this field contains the path to the file
     */
    private String filePath;
    /**
     * this field contains the size of the file
     */
    private long fileSize;

    @OneToOne
    private Shelter shelter;

    public String getNumber() {
        return number;
    }

    public void setNumber(String name) {
        this.number = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationMap that = (LocationMap) o;
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
