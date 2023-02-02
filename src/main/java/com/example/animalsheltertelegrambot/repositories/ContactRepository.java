package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
}
