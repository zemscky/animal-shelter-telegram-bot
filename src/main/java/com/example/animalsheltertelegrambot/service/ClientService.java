package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.repositories.AnimalRepository;
import com.example.animalsheltertelegrambot.repositories.ClientRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ClientService {

    private final Logger logger = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;
    private final AnimalRepository animalRepository;
    private TelegramBot telegramBot;

    public ClientService(ClientRepository clientRepository, AnimalRepository animalRepository) {
        this.clientRepository = clientRepository;
        this.animalRepository = animalRepository;
    }

    public void setTelegramBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendGreetings(Update update) {
        logger.info("Sending the greeting message");
        long chatId = update.message().chat().id();
        SendMessage message = new SendMessage(chatId,
                "Привет! Я бот приюта для животных");
        SendResponse response = telegramBot.execute(message);
        if (!response.isOk()) {
            logger.error("Could not send the greeting message! " +
                    "Error code: {}", response.errorCode());
        }
    }

    public void sendShelterDescription(Update update) throws IOException {
        logger.info("Sending the description about the Shelter");
        long chatId = update.message().chat().id();
        SendMessage message = new SendMessage(chatId,
                getContentAsString("src/main/resources/shelter-description.txt"));
        SendResponse response = telegramBot.execute(message);
        if (!response.isOk()) {
            logger.error("Could not send the shelter description message! " +
                    "Error code: {}", response.errorCode());
        }
    }

    private String getContentAsString(String path) throws IOException {
        try {
            String content = new String(Files.readAllBytes(Paths.get(path)));
            return content;
        } catch (IOException e) {
            logger.error("Error: The path not found");
            return "Information is not available at the moment, please try again later";
        }
    }
}
