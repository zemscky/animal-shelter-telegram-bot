package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.repositories.AnimalRepository;
import com.example.animalsheltertelegrambot.repositories.ClientRepository;
import com.example.animalsheltertelegrambot.repositories.ContactRepository;
import com.example.animalsheltertelegrambot.repositories.InfoMessageRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;


@Service
public class MessageSender {

    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    private final ClientRepository clientRepository;
    private final AnimalRepository animalRepository;
    private final InfoMessageRepository messageRepository;
    private final ContactRepository contactRepository;
    private static TelegramBot telegramBot;

    public MessageSender(ClientRepository clientRepository, AnimalRepository animalRepository, InfoMessageRepository messageRepository, ContactRepository contactRepository) {
        this.clientRepository = clientRepository;
        this.animalRepository = animalRepository;
        this.messageRepository = messageRepository;
        this.contactRepository = contactRepository;
    }

    public static void setTelegramBot(TelegramBot telegramBot) {
        MessageSender.telegramBot = telegramBot;
    }

    /**
     * sends a confirmation that we have accepted a push-button response from the user
     *
     * @param id is argument from update.callbackQuery().id()
     */
    public static BaseResponse sendCallbackQueryResponse(String id) {
        return telegramBot.execute(new AnswerCallbackQuery(id));
    }

    /**
     * sends message to the user and performs logging
     *
     * @param chatId user's chatId
     * @param name message's name for logger
     * @param text text for sending
     * @param keyboardMarkup if it is not null, then we send the keyboard to the user
     * @return
     */
    public static void sendMessage(Long chatId, String name, String text,
                                   InlineKeyboardMarkup keyboardMarkup) {
        logger.info("Sending the " + name + " message");

        SendMessage sm = new SendMessage(chatId, text);
        SendResponse response;

        if (keyboardMarkup == null) {
            response = telegramBot.execute(sm);
        } else {
            logger.info("Sending the keyboard");
            response = telegramBot.execute(sm.replyMarkup(keyboardMarkup));
        }
        if (!response.isOk()) {
            logger.error("Could not send the " + name + " message! " +
                    "Error code: {}", response.errorCode());
        }
    }

    public static void sendMessage(Long chatId, String nameOfMessageForLogger, String text) {
        sendMessage(chatId, nameOfMessageForLogger, text, null);
    }

    public static void sendMessage(Long chatId, String text) {
        sendMessage(chatId, text, text, null);
    }

    public static void sendMessage(SendMessage sendMessage) {
        telegramBot.execute(sendMessage);
    }

    public static void sendPhoto(Long chatId, String caption, String imagePath) {
        try {
            File image = ResourceUtils.getFile("classpath:" + imagePath);
            SendPhoto sendPhoto = new SendPhoto(chatId, image);
            sendPhoto.caption(caption);
            telegramBot.execute(sendPhoto);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void sendPhoto(SendPhoto sendPhoto) {
        telegramBot.execute(sendPhoto);
    }
}
