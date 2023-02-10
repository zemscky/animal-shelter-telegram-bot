package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.ShelterUser;
import com.example.animalsheltertelegrambot.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
    private final UserRepository userRepository;

    public ReportService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isReportService(String userMessage, Long chatId) {
        ShelterUser user = userRepository.findById(chatId).orElseThrow(RuntimeException::new);
        return userMessage.equals("/sendReport");
    }

    public void sendReportMessage() {
//        ShelterUser user = this.userRepository.findById(chatId).orElseThrow(RuntimeException::new);
    }
}
