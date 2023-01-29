package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.InfoMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InfoMessageRepository extends JpaRepository<InfoMessage, String> {

    @Query(value = "select safety from general_info where id = 1", nativeQuery = true)
    String findSafetyRules();
}
