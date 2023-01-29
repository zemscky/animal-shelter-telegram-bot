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

import static com.example.animalsheltertelegrambot.models.MessageId.*;

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

    public void sendGreetings(Update update) {
        sendDescription(update, this.messageRepository.findById(GREETINGS.getId()).
                orElse(getNotFoundMessage()));
    }

    public void sendShelterDescription(Update update) {
        sendDescription(update, this.messageRepository.findById(SHELTER_DESCRIPTION.getId())
                .orElse(getNotFoundMessage()));
    }

    public void sendCallBackMessage(Update update) {
        sendDescription(update, this.messageRepository.findById(CALLBACK.getId())
                .orElse(getNotFoundMessage()));
    }

    private ShelterMessage getNotFoundMessage() {
        ShelterMessage sm = new ShelterMessage();
        sm.setDescription("not found message");
        sm.setMessageText("Information not found, please try again later");
        return sm;
    }

    private void sendDescription(Update update, ShelterMessage shelterMessage) {
        logger.info("Sending the " + shelterMessage.getDescription() + " message");
        SendResponse response = telegramBot.execute(
                new SendMessage(
                update.message().chat().id(),
                shelterMessage.getMessageText()));
        if (!response.isOk()) {
            logger.error("Could not send the " + shelterMessage.getDescription() + " message! " +
                    "Error code: {}", response.errorCode());
        }
    }
}
