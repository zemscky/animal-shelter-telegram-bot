package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.ShelterUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ShelterUserRepository extends JpaRepository<ShelterUser, Long> {

    Optional<ShelterUser> findByUsername(String username);
}
