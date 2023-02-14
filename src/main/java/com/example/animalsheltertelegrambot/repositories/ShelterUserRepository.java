package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.ShelterUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ProbationPeriodRepository is an interface for storing animal shelter user information
 * Corresponds to the shelter_user table in PostgreSQL.
 * Extends {@link JpaRepository}
 * @see ShelterUser shelterUser
 */
@Repository
public interface ShelterUserRepository extends JpaRepository<ShelterUser, Long> {
}
