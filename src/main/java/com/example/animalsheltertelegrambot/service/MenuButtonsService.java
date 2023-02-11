package com.example.animalsheltertelegrambot.service;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class MenuButtonsService {

    public static final String GENERAL_INFO_MENU = "Узнать о приюте";
    public static final String ADOPTION_MENU = "Как забрать питомца";
    public static final String SEND_REPORT = "Отправить отчёт";
    public static final String VOLUNTEER = "Позвать волонтёра";
    public static final String CALLBACK = "Запросить обратный звонок";
    public static final String ABOUT_SHELTER = "О нас";
    public static final String ADDRESS_SCHEDULE = "Адрес и часы работы";
    public static final String SAFETY = "Техника безопасности";
    public static final String DOCUMENTS_FOR_ADOPTION = "Необходимые документы";
    public static final String CYNOLOGIST_ADVICE = "Советы кинолога";
    public static final String REFUSAL_REASONS = "Причины отказа";

    private final Logger logger = LoggerFactory.getLogger(MenuButtonsService.class);

    public InlineKeyboardMarkup createKeyboardMarkup(String... buttonNames) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        Arrays.stream(buttonNames).forEach(buttonName -> {
            keyboardMarkup.addRow(
                    new InlineKeyboardButton(buttonName)
                            .callbackData(buttonName));
        });
        return keyboardMarkup;
    }



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
}
