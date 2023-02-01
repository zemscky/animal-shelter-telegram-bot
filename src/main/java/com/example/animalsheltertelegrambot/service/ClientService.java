package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.InfoMessage;
import com.example.animalsheltertelegrambot.repositories.AnimalRepository;
import com.example.animalsheltertelegrambot.repositories.ClientRepository;
import com.example.animalsheltertelegrambot.repositories.InfoMessageRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * Contains business logic regarding processing user`s messages and commands.
 */
@Service
public class ClientService {

    private final Logger logger = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;
    private final AnimalRepository animalRepository;
    private final InfoMessageRepository messageRepository;
    private TelegramBot telegramBot;

    public ClientService(ClientRepository clientRepository, AnimalRepository animalRepository, InfoMessageRepository messageRepository) {
        this.clientRepository = clientRepository;
        this.animalRepository = animalRepository;
        this.messageRepository = messageRepository;
    }

    public void setTelegramBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    /**
     * Finds an informational message in the database by the command received
     * from user which serves as a primary key. If user`s message is not
     * a command, or the command was not found method sends a message
     * stating that requested information was not found.
     *
     * @param update new message from user
     * @see ClientService#getNotFoundMessage()
     */
    public void sendMessage(Update update) {

        if (update.callbackQuery() != null) {
            if (update.callbackQuery().data().equals("Узнать о приюте")) {
                InfoMessage infoMessage = this.messageRepository.
                        findById("/generalmenu").
                        orElse(getNotFoundMessage());
                SendResponse response = telegramBot.execute(
                        new SendMessage(
                                update.callbackQuery().message().chat().id(),
                                infoMessage.getText()));
            }

        } else if (update.message() != null) {

            Message message = update.message();
            if (message.text().equals("/start")) {
                sendMenuButtons(message);
//                clientService.sendGreetings(update);
            } else {
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

        }



    }

    private InfoMessage getNotFoundMessage() {
        InfoMessage sm = new InfoMessage();
        sm.setTag("not found");
        sm.setText("Information not found, please try again later");
        return sm;
    }

    private void sendMenuButtons(Message message) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonGeneral = new InlineKeyboardButton("Узнать о приюте");
        InlineKeyboardButton buttonDogs = new InlineKeyboardButton("Как забрать собаку");
        InlineKeyboardButton buttonSendReport = new InlineKeyboardButton("Отправить отчёт");
        InlineKeyboardButton buttonVolunteer = new InlineKeyboardButton("Позвать волонтёра");

        buttonGeneral.callbackData("Узнать о приюте");
        buttonDogs.callbackData(buttonDogs.text());
        buttonSendReport.callbackData(buttonSendReport.text());
        buttonVolunteer.callbackData(buttonVolunteer.text());

        keyboardMarkup.addRow(buttonGeneral);
        keyboardMarkup.addRow(buttonDogs);
        keyboardMarkup.addRow(buttonSendReport);
        keyboardMarkup.addRow(buttonVolunteer);

        logger.info("Menu buttons created");

        SendResponse response = telegramBot.execute(new SendMessage(message
                .chat().id(), "Выберите, пожалуйста, раздел").replyMarkup(keyboardMarkup));
    }
}
