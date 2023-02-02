package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.repositories.AnimalRepository;
import com.example.animalsheltertelegrambot.repositories.ClientRepository;
import com.example.animalsheltertelegrambot.repositories.InfoMessageRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    public static final String GENERAL_INFO = "Узнать о приюте";
    public static final String DOG_INFO = "Как забрать собаку";
    public static final String SEND_REPORT = "Отправить отчёт";
    public static final String VOLUNTEER = "Позвать волонтёра";
    public static final String CALLBACK = "Позвоните мне";

    private final Logger logger = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;
    private final AnimalRepository animalRepository;
    private final InfoMessageRepository messageRepository;
    private final CommandService commandService;
    private TelegramBot telegramBot;

    public ClientService(ClientRepository clientRepository, AnimalRepository animalRepository, InfoMessageRepository messageRepository, CommandService commandService) {
        this.clientRepository = clientRepository;
        this.animalRepository = animalRepository;
        this.messageRepository = messageRepository;
        this.commandService = commandService;
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
     * @see CommandService#getNotFoundInfoMessage()
     */

    public void sendMessage(Update update) {
        if (update.message() != null) {
            Long chatId = update.message().chat().id();
            String text = update.message().text();
            if (text.equals("/start")) {
                InlineKeyboardMarkup keyboardMarkup = createMenuButtons();
                this.commandService.sendResponseToCommand(chatId, text, keyboardMarkup);
            } else {
                this.commandService.sendResponseToCommand(chatId, text);
            }
        } else if (update.callbackQuery() != null) {
            Long chatId = update.callbackQuery().message().chat().id();
            String text = update.callbackQuery().data();
            sendSectionMenu(chatId, text);
        }
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

    public void sendSectionMenu(Long chatId, String text) {
        switch (text) {
            case GENERAL_INFO -> this.commandService.sendResponseToCommand(chatId, "/generalmenu");
            case DOG_INFO -> this.commandService.sendResponseToCommand(chatId, "/dogmenu");
            case SEND_REPORT -> this.commandService.sendResponseToCommand(chatId, "/sendreportmenu");
            case VOLUNTEER -> this.commandService.sendResponseToCommand(chatId, "/volunteer");
            case CALLBACK -> this.commandService.sendResponseToCommand(chatId, "/callback");
            default -> this.commandService.sendResponseToCommand(chatId, "not found");
        }
    }
}
