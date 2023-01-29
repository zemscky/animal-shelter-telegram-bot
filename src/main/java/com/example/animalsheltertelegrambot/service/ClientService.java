package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.ShelterMessage;
import com.example.animalsheltertelegrambot.repositories.AnimalRepository;
import com.example.animalsheltertelegrambot.repositories.ClientRepository;
import com.example.animalsheltertelegrambot.repositories.ShelterMessageRepository;
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
    private final ShelterMessageRepository messageRepository;
    private TelegramBot telegramBot;

    public ClientService(ClientRepository clientRepository, AnimalRepository animalRepository, ShelterMessageRepository messageRepository) {
        this.clientRepository = clientRepository;
        this.animalRepository = animalRepository;
        this.messageRepository = messageRepository;
    }

    public void setTelegramBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendMessage(Update update) {
        logger.info("Sending the " + update.message().text() + " message");

        ShelterMessage shelterMessage = this.messageRepository.
                findShelterMessageByTag(update.message().text()).
                orElse(getNotFoundMessage());
        SendResponse response = telegramBot.execute(
                new SendMessage(
                update.message().chat().id(),
                shelterMessage.getMessageText()));

        if (!response.isOk()) {
            logger.error("Could not send the " + shelterMessage.getTag() + " message! " +
                    "Error code: {}", response.errorCode());
        }
    }

    private ShelterMessage getNotFoundMessage() {
        ShelterMessage sm = new ShelterMessage();
        sm.setTag("not found message");
        sm.setMessageText("Information not found, please try again later");
        return sm;
    }
}
