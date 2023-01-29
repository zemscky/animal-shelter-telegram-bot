package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
