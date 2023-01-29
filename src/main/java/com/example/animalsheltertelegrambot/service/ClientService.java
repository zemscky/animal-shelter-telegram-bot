package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.repositories.AnimalRepository;
import com.example.animalsheltertelegrambot.repositories.ClientRepository;
import com.example.animalsheltertelegrambot.repositories.InfoMessageRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final Logger logger = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;
    private final AnimalRepository animalRepository;
    private final InfoMessageRepository infoMessageRepository;
    private TelegramBot telegramBot;

    public ClientService(ClientRepository clientRepository,
                         AnimalRepository animalRepository,
                         InfoMessageRepository infoMessageRepository) {
        this.clientRepository = clientRepository;
        this.animalRepository = animalRepository;
        this.infoMessageRepository = infoMessageRepository;
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

    public void sendMessage(Update update) {
//        logger.info("Sending the safety requirements");
        long chatId = update.message().chat().id();
        String text = infoMessageRepository.findById(update.message().text()).get();
        SendMessage message = new SendMessage(chatId,
                text);
        SendResponse response = telegramBot.execute(message);
        if (!response.isOk()) {
            logger.error("Could not send the safety requirements! " +
                    "Error code: {}", response.errorCode());
        }
    }
}
