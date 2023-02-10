package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.repositories.AnimalRepository;
import com.example.animalsheltertelegrambot.repositories.AdopterRepository;
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

@Service
public class UserService {
    public static final String GENERAL_INFO = "Узнать о приюте";
    public static final String DOG_INFO = "Как забрать собаку";
    public static final String SEND_REPORT = "Отправить отчёт";
    public static final String VOLUNTEER = "Позвать волонтёра";
    public static final String CALLBACK = "Запросить обратный звонок";

    public static final String ABOUT_SHELTER = "Подробнее о приюте";
    public static final String ADDRESS_SCHEDULE = "Адрес и часы работы";
    public static final String SAFETY = "Техника безопасности";

    public static final String DOCUMENTS_FOR_ADOPTION = "Необходимые документы";
    public static final String CYNOLOGIST_ADVICE = "Советы кинолога";
    public static final String ACTION_FAILURE = "Почему вам могут отказать";
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final AdopterRepository adopterRepository;
    private final AnimalRepository animalRepository;
    private final InfoMessageRepository messageRepository;
    private final CommandService commandService;
    private final FileService fileService;

    public UserService(AdopterRepository adopterRepository, AnimalRepository animalRepository, InfoMessageRepository messageRepository, CommandService commandService, FileService fileService) {
        this.adopterRepository = adopterRepository;
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
            Long chatId = update.message().chat().id();
            Document document = update.message().document();
            String fileId = document.fileId();
            String fileName = document.fileName();
            long fileSize = document.fileSize();
            try {
                fileService.uploadPhotoShelter(chatId, fileId, fileName, fileSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (update.message().photo() != null && update.message().caption() != null) {
            String fileName = update.message().caption();
            Long chatId = update.message().chat().id();
            PhotoSize[] photos = update.message().photo();
            PhotoSize photo = Arrays.stream(photos).max(Comparator.comparing(PhotoSize::fileSize)).orElse(null);
            String fileId = photo.fileId();
            long fileSize = photo.fileSize();
            if (fileName.contains("shelter-")) {
                try {
                    fileService.uploadPhotoShelter(chatId, fileId, fileName, fileSize);
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
    private InlineKeyboardMarkup createMenuButtonsGeneralDogInfo() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton documentsForAdoption = new InlineKeyboardButton(DOCUMENTS_FOR_ADOPTION);
        InlineKeyboardButton cynologistAdvice = new InlineKeyboardButton(CYNOLOGIST_ADVICE);
        InlineKeyboardButton actionFailure = new InlineKeyboardButton(ACTION_FAILURE);

        documentsForAdoption.callbackData(documentsForAdoption.text());
        cynologistAdvice.callbackData(cynologistAdvice.text());
        actionFailure.callbackData(actionFailure.text());

        inlineKeyboardMarkup.addRow(documentsForAdoption);
        inlineKeyboardMarkup.addRow(cynologistAdvice);
        inlineKeyboardMarkup.addRow(actionFailure);

        return inlineKeyboardMarkup;
    }

    public void sendSectionMenu(Long chatId, String text) {
        switch (text) {
            case GENERAL_INFO -> {
                InlineKeyboardMarkup keyboardMarkup = createMenuButtonsGeneralInfo();
                this.commandService.sendResponseToCommand(chatId, "/description", keyboardMarkup);
            }
            case DOG_INFO -> {
                InlineKeyboardMarkup inlineKeyboardButton = createMenuButtonsGeneralDogInfo();
                this.commandService.sendResponseToCommand(chatId, "/dogmenu", inlineKeyboardButton);
            }
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
            case DOCUMENTS_FOR_ADOPTION -> this.commandService.sendResponseToCommand(chatId,"/documents");
            case CYNOLOGIST_ADVICE -> this.commandService.sendResponseToCommand(chatId,"/advice");
            case ACTION_FAILURE -> this.commandService.sendResponseToCommand(chatId,"/refusal");
            default -> this.commandService.sendResponseToCommand(chatId, "not found");
        }
    }
}
