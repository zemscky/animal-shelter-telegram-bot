package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.LocationMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationMapRepository extends JpaRepository<LocationMap, String> {

//    Optional<LocationMap> findByShelterId(String shelter_number);

    Optional<LocationMap> findByShelterNumber(String shelterNumber);
}
