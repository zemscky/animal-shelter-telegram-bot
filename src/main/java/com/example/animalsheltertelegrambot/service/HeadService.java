package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.repositories.*;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

//@Service
//public class HeadService {
//
//    private final Logger logger = LoggerFactory.getLogger(HeadService.class);
//
//    private TelegramBot telegramBot;
////    private final AdopterRepository adopterRepository;
//    private final ClientService clientService;
////    private final AnimalRepository animalRepository;
////    private final InfoMessageRepository infoMessageRepository;
//    private final CommandService commandService;
//    private final FileService fileService;
//    private final ReportService reportService;
//    private final MenuButtonsService menuButtonsService;
//    private final ShelterRepository shelterRepository;
//    private final InfoMessageService infoMessageService;
//    private final BotExecutionService botExecutionService;
//
//    public HeadService(AdopterRepository adopterRepository, ClientService clientService, AnimalRepository animalRepository, InfoMessageRepository infoMessageRepository, CommandService commandService, FileService fileService, ReportService reportService, MenuButtonsService menuButtonsService, ShelterRepository shelterRepository, InfoMessageService infoMessageService, BotExecutionService botExecutionService) {
////        this.adopterRepository = adopterRepository;
//        this.clientService = clientService;
//        this.reportService = reportService;
////        this.animalRepository = animalRepository;
////        this.infoMessageRepository = infoMessageRepository;
//        this.commandService = commandService;
//        this.fileService = fileService;
//        this.menuButtonsService = menuButtonsService;
//        this.shelterRepository = shelterRepository;
//        this.infoMessageService = infoMessageService;
//        this.botExecutionService = botExecutionService;
//    }
//
//    public void setTelegramBot(TelegramBot telegramBot) {
//        this.telegramBot = telegramBot;
//    }
//
//    public void analyzeMessageAndRedirect(Message message) {
//        Long chatId = message.chat().id();
//        if (!clientService.clientExists(chatId)) {
//            clientService.saveClient(chatId);
//            sendShelterMenu(chatId);
//        }
//    }
//
//    public void analyzeCallbackQueryAndRedirect(CallbackQuery callbackQuery) {
//        Long chatId = callbackQuery.message().chat().id();
//    }
//
//    public void sendShelterMenu(Long chatId) {
//        String[] buttonNames = shelterRepository.findAllSheltersNames();
//        InlineKeyboardMarkup keyboardMarkup = menuButtonsService
//                .createKeyboardMarkup(buttonNames);
//        String text = infoMessageService.getInfoMessage("/start").getText();
//        SendMessage sm = new SendMessage(chatId, text).replyMarkup(keyboardMarkup);
//        botExecutionService.sendMessage(sm);
//    }
//
//
//
//
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
//            } else {
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
//        } else if (update.message().photo() != null && update.message().caption() != null) {
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
//}
