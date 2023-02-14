package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.Adopter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * AdopterRepository is an interface for storing information about the adopter.
 * Corresponds to the adopter table in PostgreSQL.
 * Extends {@link JpaRepository}
 * @see Adopter adoper
 */
@Repository
public interface AdopterRepository extends JpaRepository<Adopter, Long> {

    Optional<Adopter> findAdopterByUsername(String username);
}
