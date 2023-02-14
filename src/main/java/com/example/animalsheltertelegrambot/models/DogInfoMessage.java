package com.example.animalsheltertelegrambot.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class DogInfoMessage implements AnimalInfoMessage{
    /**
     * this field contains the tag of the DogInfoMessage and
     * is the primary key of the dog_info_message table in PostgreSQL
     */
    @Id
    private String tag;
    /**
     * this field contains text
     */
    private String text;

    public DogInfoMessage() {
    }

    public DogInfoMessage(String tag, String text) {
        this.tag = tag;
        this.text = text;
    }

    public String getTag() {
        return tag;
    }

    public String getText() {
        return text;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DogInfoMessage that = (DogInfoMessage) o;
        return Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }
}
