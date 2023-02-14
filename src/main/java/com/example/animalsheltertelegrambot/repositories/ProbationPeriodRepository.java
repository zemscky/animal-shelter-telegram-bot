package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.ProbationPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ProbationPeriodRepository is an interface for storing probation information for an adopter
 * Corresponds to the probation_period table in PostgreSQL.
 * Extends {@link JpaRepository}
 * @see ProbationPeriod probationPeriod
 */
@Repository
public interface ProbationPeriodRepository extends JpaRepository<ProbationPeriod, Long> {

}
