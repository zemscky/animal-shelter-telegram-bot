package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.Contact;
import com.example.animalsheltertelegrambot.models.InfoMessage;
import com.example.animalsheltertelegrambot.repositories.AnimalRepository;
import com.example.animalsheltertelegrambot.repositories.UserRepository;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class CommandService {

    private final Logger logger = LoggerFactory.getLogger(CommandService.class);

    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;
    private final InfoMessageRepository messageRepository;
    private final ContactRepository contactRepository;
    private TelegramBot telegramBot;

    public CommandService(UserRepository userRepository, AnimalRepository animalRepository, InfoMessageRepository messageRepository, ContactRepository contactRepository) {
        this.userRepository = userRepository;
        this.animalRepository = animalRepository;
        this.messageRepository = messageRepository;
        this.contactRepository = contactRepository;
    }

    public void setTelegramBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    /**
     * Accepts the request with {@link CommandService#sendResponseToCommand} and determine what the client wants
     *
     * @param chatId this is the user's chat ID
     * @param text is the text to be processed, the command, the phone number, etc.
     * @param keyboardMarkup if it is not null, then we send the keyboard to the user
     *
     * @see CommandService#isCommand
     * @see CommandService#isInfoRequest
     * @see CommandService#isMobileNumberValid
     */
    public void sendResponseToCommand(Long chatId, String text, InlineKeyboardMarkup keyboardMarkup) {
        logger.info("request processing");
        if (isCommand(text)) {
            if (isInfoRequest(text)) {
                sendInfoMessage(chatId, text, keyboardMarkup);
            } else if (text.equals("/callback")) {
                sendCallbackMessage(chatId, keyboardMarkup);
            }
        } else if (isMobileNumberValid(text)) {
            saveContact(chatId, text);
            sendContactSavedMessage(chatId);
            sendCallbackMessage(chatId, keyboardMarkup);
        } else {
            sendMessage(chatId, "unknown command", "Unknown command!", keyboardMarkup);
        }
    }

    /**
     * if there is no need to send a keyboard,
     * this auxiliary method substitutes a null value in the keyboard argument
     * {@link CommandService#sendResponseToCommand(Long, String, InlineKeyboardMarkup)}  }
     */
    public void sendResponseToCommand(Long chatId, String text) {
        sendResponseToCommand(chatId, text, null);
    }

    /**
     * Finds an informational message in the database by the command received
     * from user which serves as a primary key and sends to user {@link CommandService#sendInfoMessage}. If user`s message is not
     * a command, or the command was not found method sends a message
     * stating that requested information was not found.
     *
     * @param chatId user's chatId
     * @param tag primary key of message in database
     * @param keyboardMarkup if it is not null, then we send the keyboard to the user
     * @see CommandService#getNotFoundInfoMessage()
     */
    public SendResponse sendInfoMessage(Long chatId, String tag, InlineKeyboardMarkup keyboardMarkup) {
        InfoMessage infoMessage = this.messageRepository.
                findById(tag).
                orElse(getNotFoundInfoMessage());
        return sendMessage(chatId, tag, infoMessage.getText(), keyboardMarkup);
    }

    /**
     * sends a callback message.
     * If <u>contactRepository.findById(chatId).isPresent()</u>
     * is false, then sends {@link CommandService#sendMessage} "contact not found" message to user
     *
     * @param chatId user's chat id for sending
     * @param keyboardMarkup if it is not null, then we send the keyboard to the user
     */
    public SendResponse sendCallbackMessage(Long chatId, InlineKeyboardMarkup keyboardMarkup) {
        if (this.contactRepository.findById(chatId).isPresent()) {
            return sendMessage(chatId, "callback", "Запрос принят. Так же, Вы всегда можете прислать новый номер для обратной связи", keyboardMarkup);
        } else {
            return sendMessage(chatId, "contact not found", "Пожалуйста, напишите номер телефона(без отступов и разделяющих знаков) для обратной связи", null);
        }
    }

    /**
     * sends "The number is saved" message to user with
     * {@link CommandService#sendMessage}
     *
     * @param chatId user's chat id
     */
    public SendResponse sendContactSavedMessage(Long chatId) {
        return sendMessage(chatId, "contact saved", "The number is saved", null);
    }

    /**
     * sends a confirmation that we have accepted a push-button response from the user
     *
     * @param id is argument from update.callbackQuery().id()
     */
    public BaseResponse sendCallbackQueryResponse(String id) {
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
    public SendResponse sendMessage(Long chatId, String name, String text,
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
        return response;
    }

    public void SendPhoto(Long chatId, String caption, String imagePath) {
        try {
            File image = ResourceUtils.getFile("classpath:" + imagePath);
            SendPhoto sendPhoto = new SendPhoto(chatId, image);
            sendPhoto.caption(caption);
            telegramBot.execute(sendPhoto);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * checks whether the incoming text is a command(starts with '/')
     * @param text argument for check
     */
    public boolean isCommand(String text) {
        return text.startsWith("/");
    }

    /**
     * checks whether the incoming text is a request for information
     * if infoMessageRepository.findById(tag).isPresent() then return true
     * @param tag tag for searching
     * @Return boolean (is request or not)
     */
    public boolean isInfoRequest(String tag) {
        return this.messageRepository.findById(tag).isPresent();
    }

    /**
     * checks with pattern whether the incoming text is a phone number
     *
     * @param number field for check
     */
    public boolean isMobileNumberValid(String number) {
        Pattern p = Pattern.compile("^\\+?[78][-(]?\\d{10}$");
        Matcher m = p.matcher(number);
        return m.matches();
    }

    /**
     * creates a new contact and puts it in the database
     *
     * @param chatId user's chat id
     * @param mobileNumber user's number of phone
     */
    public Contact saveContact(Long chatId, String mobileNumber) {
        return this.contactRepository.save(new Contact(chatId, mobileNumber));
    }

    /**
     * returns an information message that was not found for further sending to the user
     * @return new InfoMessage("not found", "Information not found, please try again later")
     */
    public InfoMessage getNotFoundInfoMessage() {
        return new InfoMessage("not found", "Information not found, please try again later");
    }
}
