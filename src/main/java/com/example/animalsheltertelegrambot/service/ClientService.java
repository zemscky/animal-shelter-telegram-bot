package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.InfoMessage;
import com.example.animalsheltertelegrambot.repositories.AnimalRepository;
import com.example.animalsheltertelegrambot.repositories.ClientRepository;
import com.example.animalsheltertelegrambot.repositories.InfoMessageRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
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

    public static final String GENERAL_INFO = "Узнать о приюте";
    public static final String DOG_INFO = "Как забрать собаку";
    public static final String SEND_REPORT = "Отправить отчёт";
    public static final String VOLUNTEER = "Позвать волонтёра";

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

    public static String GENERAL_INFO(Update update) {
        return GENERAL_INFO;
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

        if (update.message() != null) {
            Long chatId = update.message().chat().id();
            String text = update.message().text();
            if (text.equals("/start")) {
                InlineKeyboardMarkup keyboardMarkup = createMenuButtons();
                sendResponseToCommand(chatId, text, keyboardMarkup);
            } else if (update.callbackQuery() != null) {
                extracted(update);

                sendResponseToCommand(chatId, text);
            }
        } else if (update.callbackQuery() != null) {
            Long chatId = update.callbackQuery().message().chat().id();
            String text = update.callbackQuery().data();
            sendSectionMenu(chatId, text);
        }
    }

    private InfoMessage getNotFoundMessage() {
        InfoMessage sm = new InfoMessage();
        sm.setTag("not found");
        sm.setText("Information not found, please try again later");
        return sm;
    }

    private InlineKeyboardMarkup createMenuButtons() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton generalButton = new InlineKeyboardButton(GENERAL_INFO);
        InlineKeyboardButton dogsButton = new InlineKeyboardButton(DOG_INFO);
        InlineKeyboardButton sendReportButton = new InlineKeyboardButton(SEND_REPORT);
        InlineKeyboardButton volunteerButton = new InlineKeyboardButton(VOLUNTEER);

        generalButton.callbackData(generalButton.text());
        dogsButton.callbackData(dogsButton.text());
        sendReportButton.callbackData(sendReportButton.text());
        volunteerButton.callbackData(volunteerButton.text());

        keyboardMarkup.addRow(generalButton);
        keyboardMarkup.addRow(dogsButton);
        keyboardMarkup.addRow(sendReportButton);
        keyboardMarkup.addRow(volunteerButton);

        return keyboardMarkup;
    }

    public  InlineKeyboardMarkup stepOne(Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton generalButton = new InlineKeyboardButton("Наш адресс");
        generalButton.callbackData(generalButton.text());
        keyboardMarkup.addRow(generalButton);
        return keyboardMarkup;
    }

    private void extracted(Update update) {
        String data = update.callbackQuery().data();
        switch (data) {
            case ("info"):
                stepOne(update);
                break;
            default:
                break;
        }
    }

    public void sendResponseToCommand(Long chatId, String text) {
        sendResponseToCommand(chatId, text, null);
    }

    public void sendResponseToCommand(Long chatId, String text,
                                      InlineKeyboardMarkup keyboardMarkup) {

        logger.info("Sending the " + text + " message");

        InfoMessage infoMessage = this.messageRepository.
                findById(text).
                orElse(getNotFoundMessage());

        SendMessage sm = new SendMessage(chatId, infoMessage.getText());
        SendResponse response;

        if (keyboardMarkup == null) {
            response = telegramBot.execute(sm);
        } else {
            response = telegramBot.execute(sm.replyMarkup(keyboardMarkup));
        }

        if (!response.isOk()) {
            logger.error("Could not send the " + infoMessage.getTag() + " message! " +
                    "Error code: {}", response.errorCode());
        }
    }

    public void sendSectionMenu(Long chatId, String text) {

        switch (text) {
            case GENERAL_INFO -> sendResponseToCommand(chatId, "/generalmenu");
            case DOG_INFO -> sendResponseToCommand(chatId, "/dogmenu");
            case SEND_REPORT -> sendResponseToCommand(chatId, "/sendreportmenu");
            case VOLUNTEER -> sendResponseToCommand(chatId, "/volunteer");
            default -> sendResponseToCommand(chatId, "not found");
        }
    }
}
