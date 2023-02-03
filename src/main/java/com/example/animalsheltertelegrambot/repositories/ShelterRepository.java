package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, String> {
}
