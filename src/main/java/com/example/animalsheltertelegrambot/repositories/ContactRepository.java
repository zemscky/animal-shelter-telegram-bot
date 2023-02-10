package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Client, Long> {
}
