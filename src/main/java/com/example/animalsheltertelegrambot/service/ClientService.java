package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.repositories.AnimalRepository;
import com.example.animalsheltertelegrambot.repositories.ClientRepository;
import com.example.animalsheltertelegrambot.repositories.InfoMessageRepository;
import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class ClientService {
    public static final String GENERAL_INFO = "Узнать о приюте";
    public static final String DOG_INFO = "Как забрать собаку";
    public static final String SEND_REPORT = "Отправить отчёт";
    public static final String VOLUNTEER = "Позвать волонтёра";
    public static final String CALLBACK = "Запросить обратный звонок";

    public static final String ABOUT_SHELTER = "Подробнее о приюте";
    public static final String ADDRESS_SCHEDULE = "Адрес и часы работы";
    public static final String SAFETY = "Техника безопасности";

    private final Logger logger = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;
    private final AnimalRepository animalRepository;
    private final InfoMessageRepository messageRepository;
    private final CommandService commandService;
    private final FileService fileService;

    public ClientService(ClientRepository clientRepository, AnimalRepository animalRepository, InfoMessageRepository messageRepository, CommandService commandService, FileService fileService) {
        this.clientRepository = clientRepository;
        this.animalRepository = animalRepository;
        this.messageRepository = messageRepository;
        this.commandService = commandService;
        this.fileService = fileService;
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
        if (update.message() != null && update.message().text() != null) {
            Long chatId = update.message().chat().id();
            String text = update.message().text();
            if (text.equals("/start")) {
                InlineKeyboardMarkup keyboardMarkup = createMenuButtons();
                this.commandService.SendPhoto(chatId, "", "images/shelter/shelter_logo.jpg");
                this.commandService.sendResponseToCommand(chatId, text, keyboardMarkup);
            }  else {
                this.commandService.sendResponseToCommand(chatId, text);
            }
        } else if (update.callbackQuery() != null) {
            this.commandService.sendCallbackQueryResponse(update.callbackQuery().id());
            Long chatId = update.callbackQuery().message().chat().id();
            String text = update.callbackQuery().data();
            sendSectionMenu(chatId, text);
        } else if (update.message().document() != null) {
            Document document = update.message().document();
            String fileId = document.fileId();
            String fileName = document.fileName();
            long fileSize = document.fileSize();
            System.out.println(fileId);
            System.out.println(fileName);
            try {
                fileService.uploadPhotoShelter(fileId, fileName, fileSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (update.message().photo() != null) {
            PhotoSize[] photos = update.message().photo();
            PhotoSize photo = Arrays.stream(photos).max(Comparator.comparing(PhotoSize::fileSize)).orElse(null);
            String fileId = photo.fileId();
            String fileName = update.message().caption();
            long fileSize = photo.fileSize();
            System.out.println(fileId);
            System.out.println(fileName);
            if (fileName.contains("shelter")) {
                try {
                    fileService.uploadPhotoShelter(fileId, fileName, fileSize);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private InlineKeyboardMarkup createMenuButtons() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton generalButton = new InlineKeyboardButton(GENERAL_INFO);
        InlineKeyboardButton dogsButton = new InlineKeyboardButton(DOG_INFO);
        InlineKeyboardButton sendReportButton = new InlineKeyboardButton(SEND_REPORT);
        InlineKeyboardButton callbackRequestButton = new InlineKeyboardButton(CALLBACK);
        InlineKeyboardButton volunteerButton = new InlineKeyboardButton(VOLUNTEER);

        generalButton.callbackData(generalButton.text());
        dogsButton.callbackData(dogsButton.text());
        sendReportButton.callbackData(sendReportButton.text());
        callbackRequestButton.callbackData(callbackRequestButton.text());
        volunteerButton.callbackData(volunteerButton.text());

        keyboardMarkup.addRow(generalButton);
        keyboardMarkup.addRow(dogsButton);
        keyboardMarkup.addRow(sendReportButton);
        keyboardMarkup.addRow(callbackRequestButton);
        keyboardMarkup.addRow(volunteerButton);

        return keyboardMarkup;
    }

    private InlineKeyboardMarkup createMenuButtonsGeneralInfo() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton aboutShelterButton = new InlineKeyboardButton(ABOUT_SHELTER);
        InlineKeyboardButton addressScheduleButton = new InlineKeyboardButton(ADDRESS_SCHEDULE);
        InlineKeyboardButton safetyButton = new InlineKeyboardButton(SAFETY);

        aboutShelterButton.callbackData(aboutShelterButton.text());
        addressScheduleButton.callbackData(addressScheduleButton.text());
        safetyButton.callbackData(safetyButton.text());

        keyboardMarkup.addRow(aboutShelterButton);
        keyboardMarkup.addRow(addressScheduleButton);
        keyboardMarkup.addRow(safetyButton);

        return keyboardMarkup;
    }

    public void sendSectionMenu(Long chatId, String text) {
        switch (text) {
            case GENERAL_INFO -> {
                InlineKeyboardMarkup keyboardMarkup = createMenuButtonsGeneralInfo();
                this.commandService.sendResponseToCommand(chatId, "/description", keyboardMarkup);
            }
            case DOG_INFO -> this.commandService.sendResponseToCommand(chatId, "/dogmenu");
            case SEND_REPORT -> this.commandService.sendResponseToCommand(chatId, "/sendreportmenu");
            case ADDRESS_SCHEDULE -> {
                this.commandService.sendResponseToCommand(chatId, "/addressandschedule");
                this.commandService.SendPhoto(
                        chatId,
                        "Схема проезда к нашему приюту",
                        "images/shelter/shelter_cat_and_dog_location.jpg");
            }
            case SAFETY -> this.commandService.sendResponseToCommand(chatId, "/safety");
            case CALLBACK -> this.commandService.sendResponseToCommand(chatId, "/callback");
            case VOLUNTEER -> this.commandService.sendResponseToCommand(chatId, "/volunteer");
            case ABOUT_SHELTER -> this.commandService.sendResponseToCommand(chatId, "/aboutshelter");
            default -> this.commandService.sendResponseToCommand(chatId, "not found");
        }
    }
}
