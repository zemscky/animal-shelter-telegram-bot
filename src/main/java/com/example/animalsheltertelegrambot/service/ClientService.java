package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.Contact;
import com.example.animalsheltertelegrambot.models.InfoMessage;
import com.example.animalsheltertelegrambot.repositories.AnimalRepository;
import com.example.animalsheltertelegrambot.repositories.ClientRepository;
import com.example.animalsheltertelegrambot.repositories.ContactRepository;
import com.example.animalsheltertelegrambot.repositories.MessageRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class ClientService {

    private final Logger logger = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;
    private final AnimalRepository animalRepository;
    private final MessageRepository messageRepository;
    private final ContactRepository contactRepository;
    private TelegramBot telegramBot;

    public ClientService(ClientRepository clientRepository, AnimalRepository animalRepository, MessageRepository messageRepository, ContactRepository contactRepository) {
        this.clientRepository = clientRepository;
        this.animalRepository = animalRepository;
        this.messageRepository = messageRepository;
        this.contactRepository = contactRepository;
    }

    public void setTelegramBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    // accepts the request and determine what the client wants
    public void handleRequest(Update update) {
        logger.info("request processing");

        Long chatId = update.message().chat().id();
        String text = update.message().text();

        if (isCommand(text)) {
            if (isInfoRequest(text)) {
                sendInfoMessage(chatId, text);
            } else if (text.equals("/callback")) {
                sendCallbackMessage(chatId);
            }
        } else if (isMobileNumberValid(text)) {
            saveContact(chatId, text);
            sendContactSavedMessage(chatId);
            sendCallbackMessage(chatId);
        } else {
            sendMessage(chatId, "unknown command", "Unknown command!");
        }
    }

    // sends a callback message
    public SendResponse sendCallbackMessage(Long chatId) {
        if (this.contactRepository.findById(chatId).isPresent()) {
            return sendMessage(chatId, "callback", "Запрос принят. Так же, Вы всегда можете прислать новый номер для обратной связи");
        } else {
            return sendMessage(chatId, "contact not found", "Пожалуйста, напишите номер телефона(без отступов и разделяющих знаков) для обратной связи");
        }
    }

    //sends contact saved message
    public SendResponse sendContactSavedMessage(Long chatId) {
        return sendMessage(chatId, "contact saved", "The number is saved");
    }

    //searches for an informational message in the database and sends it to the user
    public SendResponse sendInfoMessage(Long chatId, String tag) {
        InfoMessage infoMessage = this.messageRepository.
                findById(tag).
                orElse(getNotFoundInfoMessage());
        return sendMessage(chatId, tag, infoMessage.getText());
    }

    //sends message to the user and performs logging
    public SendResponse sendMessage(Long chatId, String name, String text) {
        logger.info("Sending the " + name + " message");
        SendResponse response = telegramBot.execute(new SendMessage(chatId, text));
        if (!response.isOk()) {
            logger.error("Could not send the " + name + " message! " +
                    "Error code: {}", response.errorCode());
        }
        return response;
    }

    //checks whether the incoming text is a command
    public boolean isCommand(String text) {
        return text.startsWith("/");
    }

    //checks whether the incoming text is a request for information
    public boolean isInfoRequest(String tag) {
        return this.messageRepository.findById(tag).isPresent();
    }

    //checks whether the incoming text is a phone number
    public boolean isMobileNumberValid(String number) {
        Pattern p = Pattern.compile("^\\+?[78][-(]?\\d{10}$");
        Matcher m = p.matcher(number);
        return m.matches();
    }

    //returns an information message that was not found for further sending to the user
    public InfoMessage getNotFoundInfoMessage() {
        return new InfoMessage("not found", "Information not found, please try again later");
    }

    //creates a new contact and puts it in the database
    public Contact saveContact(Long chatId, String mobileNumber) {
        return this.contactRepository.save(new Contact(chatId, mobileNumber));
    }
}
