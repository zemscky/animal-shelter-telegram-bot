package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.ShelterMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShelterMessageRepository extends JpaRepository<ShelterMessage, Long> {

}
