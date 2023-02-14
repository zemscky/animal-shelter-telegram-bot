package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.Adopter;
import com.example.animalsheltertelegrambot.models.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 * AnimalRepository is the interface for storing Animal Shelter animals information
 * Corresponds to the animal table in PostgreSQL.
 * Extends {@link JpaRepository}
 * @see Animal Animal
 */
@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
}
