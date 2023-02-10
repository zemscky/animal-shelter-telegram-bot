package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.CatInfoMessage;
import com.example.animalsheltertelegrambot.models.InfoMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatInfoMessageRepository extends JpaRepository<CatInfoMessage, String> {
}
