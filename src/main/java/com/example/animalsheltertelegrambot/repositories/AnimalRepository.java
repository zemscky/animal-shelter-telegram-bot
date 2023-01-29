package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
}
