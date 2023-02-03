package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.InfoMessage;
import com.example.animalsheltertelegrambot.models.Shelter;
import com.example.animalsheltertelegrambot.repositories.AnimalRepository;
import com.example.animalsheltertelegrambot.repositories.ClientRepository;
import com.example.animalsheltertelegrambot.repositories.InfoMessageRepository;
import com.example.animalsheltertelegrambot.repositories.ShelterRepository;
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
    private final ShelterRepository shelterRepository;
    private final InfoMessageRepository messageRepository;
    private TelegramBot telegramBot;

    public ClientService(ClientRepository clientRepository, AnimalRepository animalRepository, ShelterRepository shelterRepository, InfoMessageRepository messageRepository) {
        this.clientRepository = clientRepository;
        this.animalRepository = animalRepository;
        this.shelterRepository = shelterRepository;
        this.messageRepository = messageRepository;
    }

    public void setTelegramBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendMessage(Update update) {
        logger.info("Sending the " + update.message().text() + " message");

        InfoMessage infoMessage = this.messageRepository.
                findById(update.message().text()).
                orElse(getNotFoundMessage());
        SendResponse response = telegramBot.execute(
                new SendMessage(
                update.message().chat().id(),
                infoMessage.getText()));

        if (!response.isOk()) {
            logger.error("Could not send the " + infoMessage.getTag() + " message! " +
                    "Error code: {}", response.errorCode());
        }
    }

    private InfoMessage getNotFoundMessage() {
        InfoMessage sm = new InfoMessage();
        sm.setTag("not found");
        sm.setText("Information not found, please try again later");
        return sm;
    }

    public void sendContacts(Update update) {
        String userMessage = update.message().text();
        Long chatId = update.message().chat().id();
        logger.info("Sending the " + userMessage + " message");
        if (userMessage.equals("/contact")) {
            logger.info("Processing update: {}", update);
            this.telegramBot.execute(
                    new SendMessage(chatId, "Для получения контактных данных приюта, введите:\n" +
                            "/contact1 - для получения контактов приюта 'Котики'\n" +
                            "/contact2 - для получения контактов приюта 'Собачки'\n" +
                            "/contact3 - для получения контактов приюта 'Жирафы'"));
        } else {
            Shelter shelter = this.shelterRepository.
                    findById(update.message().text()).
                    orElse(new Shelter());
            SendResponse response = telegramBot.execute(
                    new SendMessage(chatId,
                            "Контактные данные приюта " + "'" + shelter.getName() + "'" + "\n" +
                                    "Адрес: " + shelter.getAddress() + "\n" +
                                    "Телефон: " + shelter.getTelephoneNumber() + "\n" +
                                    "Расписание: " + shelter.getTimetable()));
            if (!response.isOk()) {
                logger.error("Could not send the " + shelter.getNumber() + " message! " +
                        "Error code: {}", response.errorCode());
            }
        }
    }
}
