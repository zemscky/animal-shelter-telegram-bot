package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    @Query("SELECT a FROM Animal a WHERE a.species = ?1")
    Animal getAnimalBy(Animal.AnimalSpecies species);
}
