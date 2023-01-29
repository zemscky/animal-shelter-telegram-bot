package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.ShelterMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShelterMessageRepository extends JpaRepository<ShelterMessage, String> {
    Optional<ShelterMessage> findShelterMessageByTag(String tag);
}
