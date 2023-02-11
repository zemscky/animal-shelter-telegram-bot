package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.InfoMessage;
import com.example.animalsheltertelegrambot.repositories.AdopterRepository;
import com.example.animalsheltertelegrambot.repositories.AnimalRepository;
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
public class InfoMessageService {
    private final Logger logger = LoggerFactory.getLogger(InfoMessageService.class);

    private final AdopterRepository adopterRepository;
    private final AnimalRepository animalRepository;
    private final InfoMessageRepository infoMessageRepository;
    private final CommandService commandService;
    private final FileService fileService;

    public InfoMessageService(AdopterRepository adopterRepository, AnimalRepository animalRepository, InfoMessageRepository infoMessageRepository, CommandService commandService, FileService fileService) {
        this.adopterRepository = adopterRepository;
        this.animalRepository = animalRepository;
        this.infoMessageRepository = infoMessageRepository;
        this.commandService = commandService;
        this.fileService = fileService;
    }

//    /**
//     * Finds an informational message in the database by the command received
//     * from user which serves as a primary key. If user`s message is not
//     * a command, or the command was not found method sends a message
//     * stating that requested information was not found.
//     *
//     * @param update new message from user
//     * @see CommandService#getNotFoundInfoMessage()
//     */

//    public void sendMessage(Update update) {
//        if (update.message() != null && update.message().text() != null) {
//            Long chatId = update.message().chat().id();
//            String text = update.message().text();
//            if (text.equals("/start")) {
//                InlineKeyboardMarkup keyboardMarkup = createMenuButtons();
//                this.commandService.SendPhoto(chatId, "", "images/shelter/shelter_logo.jpg");
//                this.commandService.sendResponseToCommand(chatId, text, keyboardMarkup);
//            }  else {
//                this.commandService.sendResponseToCommand(chatId, text);
//            }
//        } else if (update.callbackQuery() != null) {
//            this.commandService.sendCallbackQueryResponse(update.callbackQuery().id());
//            Long chatId = update.callbackQuery().message().chat().id();
//            String text = update.callbackQuery().data();
//            sendSectionMenu(chatId, text);
//        } else if (update.message().document() != null) {
//            Long chatId = update.message().chat().id();
//            Document document = update.message().document();
//            String fileId = document.fileId();
//            String fileName = document.fileName();
//            long fileSize = document.fileSize();
//            try {
//                fileService.uploadPhotoShelter(chatId, fileId, fileName, fileSize);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        else if (update.message().photo() != null && update.message().caption() != null) {
//            String fileName = update.message().caption();
//            Long chatId = update.message().chat().id();
//            PhotoSize[] photos = update.message().photo();
//            PhotoSize photo = Arrays.stream(photos).max(Comparator.comparing(PhotoSize::fileSize)).orElse(null);
//            String fileId = photo.fileId();
//            long fileSize = photo.fileSize();
//            if (fileName.contains("shelter-")) {
//                try {
//                    fileService.uploadPhotoShelter(chatId, fileId, fileName, fileSize);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

//    public void sendSectionMenu(Long chatId, String text) {
//        switch (text) {
//            case GENERAL_INFO -> {
//                InlineKeyboardMarkup keyboardMarkup = createMenuButtonsGeneralInfo();
//                this.commandService.sendResponseToCommand(chatId, "/description", keyboardMarkup);
//            }
//            case DOG_INFO -> {
//                InlineKeyboardMarkup inlineKeyboardButton = createMenuButtonsGeneralDogInfo();
//                this.commandService.sendResponseToCommand(chatId, "/dogmenu", inlineKeyboardButton);
//            }
//            case SEND_REPORT -> this.commandService.sendResponseToCommand(chatId, "/sendreportmenu");
//            case ADDRESS_SCHEDULE -> {
//                this.commandService.sendResponseToCommand(chatId, "/addressandschedule");
//                this.commandService.SendPhoto(
//                        chatId,
//                        "Схема проезда к нашему приюту",
//                        "images/shelter/shelter_cat_and_dog_location.jpg");
//            }
//            case SAFETY -> this.commandService.sendResponseToCommand(chatId, "/safety");
//            case CALLBACK -> this.commandService.sendResponseToCommand(chatId, "/callback");
//            case VOLUNTEER -> this.commandService.sendResponseToCommand(chatId, "/volunteer");
//            case ABOUT_SHELTER -> this.commandService.sendResponseToCommand(chatId, "/aboutshelter");
//            case DOCUMENTS_FOR_ADOPTION -> this.commandService.sendResponseToCommand(chatId,"/documents");
//            case CYNOLOGIST_ADVICE -> this.commandService.sendResponseToCommand(chatId,"/advice");
//            case ACTION_FAILURE -> this.commandService.sendResponseToCommand(chatId,"/refusal");
//            default -> this.commandService.sendResponseToCommand(chatId, "not found");
//        }
//    }

    public InfoMessage getNotFoundInfoMessage() {
        return new InfoMessage("not found", "Information not found, please try again later");
    }

    public InfoMessage getInfoMessage(String id) {
        return infoMessageRepository.
                findById(id).
                orElse(getNotFoundInfoMessage());
    }
}
