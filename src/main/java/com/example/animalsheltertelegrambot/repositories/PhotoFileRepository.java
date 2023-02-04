package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.PhotoFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoFileRepository extends JpaRepository<PhotoFile, Integer> {
}
