package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.Adopter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdopterRepository extends JpaRepository<Adopter, Long> {

    Optional<Adopter> findAdopterByUsername(String username);
}
