package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, String> {

    @Query(value = "select name from shelter", nativeQuery = true)
    String[] findAllSheltersNames();
}
