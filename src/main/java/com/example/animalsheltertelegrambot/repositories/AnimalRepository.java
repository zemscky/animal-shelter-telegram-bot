package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
}
