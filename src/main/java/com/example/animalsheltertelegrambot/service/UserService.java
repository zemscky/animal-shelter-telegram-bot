package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.ShelterType;
import com.example.animalsheltertelegrambot.models.ShelterUser;
import com.example.animalsheltertelegrambot.models.UserStatus;
import com.example.animalsheltertelegrambot.repositories.ShelterUserRepository;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

import java.util.Comparator;

@Service
public class UserService {
    private final ShelterUserRepository userRepository;
    private final InfoMessageService infoMessageService;
    private final CallbackService callbackService;
    private final ReportService reportService;
    private final FileService fileService;

    public UserService(ShelterUserRepository shelterUserRepository, InfoMessageService infoMessageService, CallbackService callbackService, ReportService reportService, FileService fileService) {
        this.userRepository = shelterUserRepository;
        this.infoMessageService = infoMessageService;
        this.callbackService = callbackService;
        this.reportService = reportService;
        this.fileService = fileService;
    }

    public void updateHandler(Update update) {
        if (update.message() != null) {

            Long chatId = update.message().chat().id();
            String userMessage = update.message().text();
            String userFirstName = update.message().chat().firstName();
            PhotoSize[] photoSize = update.message().photo();

            if (!this.userRepository.findById(chatId).isPresent()) {
                sendFirstGreetings(chatId, userFirstName);
                this.userRepository.save(new ShelterUser(chatId, UserStatus.JUST_USING, ShelterType.DOG_SHELTER, null, userFirstName));
                messageHandler(chatId, "/start");
                return;
             }

            ShelterUser user = userRepository.findById(chatId).orElse(null);
            if (user == null) {
                return;
            }

            if (userMessage != null && user.getUserStatus() == UserStatus.JUST_USING) {
                messageHandler(chatId, userMessage);
                return;
            }

            if (ReportService.isReportStatus(user)) {
                reportService.reportHandler(chatId, userMessage, photoSize);
                return;
            }
            return;
        }

        if (update.callbackQuery() != null) {
            MessageSender.sendCallbackQueryResponse(update.callbackQuery().id());
            CallbackQuery callbackQuery = update.callbackQuery();
            Long chatId2 = update.callbackQuery().message().chat().id();
            String text = update.callbackQuery().data();
            messageHandler(chatId2, MenuService.getCommandByButton(text));

        } else if (update.message().photo() != null && update.message().caption() != null) {
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

    //determines what the user wants
    private void messageHandler(Long chatId, String userMessage) {
        ShelterUser user = userRepository.findById(chatId).orElse(null);
        if (user == null) {
            return;
        }
        if (ReportService.isReportStatus(user)) {
            reportService.reportHandler(chatId, userMessage, null);
            return;
        }
        if (userMessage.startsWith("/menu")) {
            switch (userMessage) {
                case "/menuChoiceShelter" -> MenuService.sendChoiceShelterMenu(chatId);
                case "/menuMainShelter" -> MenuService.sendMainShelterMenu(chatId);
                case "/menuAboutShelter" -> MenuService.sendAboutShelterMenu(chatId);
                case "/menuAnimalGuide" -> MenuService.sendAnimalGuideMenu(chatId);
                default -> MessageSender.sendMessage(chatId, "incorrect menu request!");
            }
        } else if (userMessage.equals("/start")){
            MessageSender.sendPhoto(chatId, "", "images/shelter/shelter-cat-or-dog.jpg");
            MenuService.sendChoiceShelterMenu(chatId);
        } else if (userMessage.equals("/catShelter")) {
            user.setShelterType(ShelterType.CAT_SHELTER);
            MessageSender.sendMessage(chatId, "Вы выбрали приют для котов");
            MenuService.sendMainShelterMenu(chatId);
            this.userRepository.save(user);
        } else if (userMessage.equals("/dogShelter")) {
            MessageSender.sendMessage(chatId, "Вы выбрали приют для собак");
            user.setShelterType(ShelterType.DOG_SHELTER);
            MenuService.sendMainShelterMenu(chatId);
            this.userRepository.save(user);
        } else if (infoMessageService.isInfo(userMessage, chatId)) {
            infoMessageService.sendInfoMessage(chatId, userMessage);
        } else if (callbackService.isCallbackRequest(userMessage, chatId)) {
            callbackService.sendCallbackMessage(userMessage, chatId);
        } else if (userMessage.equals("/volunteer")) {
            MessageSender.sendMessage(chatId, "Ок, позову свободного Волонтера");
        } else if (reportService.isSendReportCommand(userMessage, chatId)) {
            reportService.sendReportFirstStep(chatId);
        } else {
            MessageSender.sendMessage(chatId, "default", "Не понимаю... попробуй /start");
        }
    }

    private void sendFirstGreetings(Long chatId, String userName) {
        MessageSender.sendPhoto(chatId, "", "images/shelter/shelter_logo.jpg");
        MessageSender.sendMessage(chatId, "first greeting", "Привет! Я бот приюта для животных.\n" +
                "Могу рассказать о приюте для животных, а так же о том, что необходимо сделать, чтобы забрать питомца из приюта.");
    }
}
