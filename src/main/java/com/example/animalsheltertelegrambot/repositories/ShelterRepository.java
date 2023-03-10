package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.Shelter;
import com.example.animalsheltertelegrambot.models.ShelterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, String> {

    @Query(value = "select name from shelter", nativeQuery = true)
    String[] findAllSheltersNames();

    Optional<Shelter> findByShelterType(ShelterType shelterType);
}
