package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.Shelter;
import com.example.animalsheltertelegrambot.models.ShelterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, String> {

    Optional<Shelter> findByShelterType(ShelterType shelterType);
}
