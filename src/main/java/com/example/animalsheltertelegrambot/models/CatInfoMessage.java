package com.example.animalsheltertelegrambot.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class CatInfoMessage implements AnimalInfoMessage{
    /**
     * this field contains the tag of the CatInfoMessage and
     * is the primary key of the cat_info_message table in PostgreSQL
     */
    @Id
    private String tag;
    /**
     * this field contains text
     */
    private String text;

    public CatInfoMessage() {
    }

    public CatInfoMessage(String tag, String text) {
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
        CatInfoMessage that = (CatInfoMessage) o;
        return Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }
}
