package com.example.animalsheltertelegrambot.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Animal {

    @Id
    @GeneratedValue
    private long id;
}
