package com.example.animalsheltertelegrambot.service;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Service;

@Service
public class MenuService {
    public static final String CAT_SHELTER = "Приют для кошек";
    public static final String DOG_SHELTER = "Приют для собак";

    public static final String GENERAL_INFO = "Узнать о приюте";
    public static final String ANIMAL_INFO = "Как забрать питомца";
    public static final String SEND_REPORT = "Отправить отчёт";
    public static final String VOLUNTEER = "Позвать волонтёра";

    public static final String ABOUT_SHELTER = "Подробнее о приюте";
    public static final String ADDRESS_SCHEDULE = "Адрес и часы работы";
    public static final String SAFETY = "Техника безопасности";
    public static final String PASS_REGISTRATION = "Оформление пропуска";
    public static final String CALLBACK = "Обратный вызов";

    public static final String YES = "ДА";
    public static final String NO = "НЕТ";


    public static void sendChoiceShelterMenu(Long chatId) {
        InlineKeyboardMarkup keyboardMarkup = createMenuButtons(CAT_SHELTER, DOG_SHELTER);
        MessageSender.sendMessage(chatId, "choice shelter menu", "Выбери раздел:", keyboardMarkup);
    }

    public static void sendMainShelterMenu(Long chatId) {
        InlineKeyboardMarkup keyboardMarkup = createMenuButtons(GENERAL_INFO, ANIMAL_INFO, SEND_REPORT, VOLUNTEER);
        MessageSender.sendMessage(chatId, "main shelter menu", "Выбери раздел:", keyboardMarkup);
    }

    public static void sendAboutShelterMenu(Long chatId) {
        InlineKeyboardMarkup keyboardMarkup = createMenuButtons(ABOUT_SHELTER, ADDRESS_SCHEDULE, SAFETY, PASS_REGISTRATION, CALLBACK, VOLUNTEER);
        MessageSender.sendMessage(chatId, "about shelter menu", "Выбери раздел:", keyboardMarkup);
    }

    public static void sendAnimalGuideMenu(Long chatId) {
        MessageSender.sendMessage(chatId, "animal guide menu", "Вот что я могу тебе рассказать:\n" +
                "\n" +
                "/datingRules - правила знакомства с животным до того, как забрать его из приюта.\n" +
                "/docList - список документов, необходимых для того, чтобы взять животное из приюта.\n" +
                "/transportRec - список рекомендаций по транспортировке животного.\n" +
                "/puppyKittyHome - список рекомендаций по обустройству дома для щенка/котенка.\n" +
                "/homeForOld - список рекомендаций по обустройству дома для взрослого животного.\n" +
                "/homeForDis - список рекомендаций по обустройству дома для животного с ограниченными возможностями (зрение, передвижение).\n" +
                "/hiDogRec - советы кинолога по первичному общению с собакой\n" +
                "/kinoRec - рекомендации по проверенным кинологам для дальнейшего обращения к ним\n" +
                "/denialReasons - список причин, почему могут отказать и не дать забрать собаку из приюта.\n" +
                "\n" +
                "Не нашли ответа на свой вопрос?\n" +
                "\n" +
                "/callback - запросить обратный звонок\n" +
                "/volunteer - позвать волонтёра в чат");
    }

    public static InlineKeyboardMarkup createMenuButtons(String ... buttons) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        for (String button : buttons) {
            InlineKeyboardButton button1 = new InlineKeyboardButton(button);
            button1.callbackData(button1.text());
            keyboardMarkup.addRow(button1);
        }
        return keyboardMarkup;
    }

    public static String getCommandByButton(String text) {
        String command;
        switch (text) {
            case CAT_SHELTER -> command = "/catShelter";
            case DOG_SHELTER -> command = "/dogShelter";

            case YES -> command = "ДА";
            case NO -> command = "НЕТ";

            case GENERAL_INFO -> command = "/menuAboutShelter";
            case ANIMAL_INFO -> command = "/menuAnimalGuide";
            case SEND_REPORT -> command = "/sendReport";
            case VOLUNTEER -> command = "/volunteer";

            case SAFETY -> command = "/safety";
            case CALLBACK -> command = "/callback";
            case ABOUT_SHELTER -> command = "/aboutshelter";
            case ADDRESS_SCHEDULE -> command = "/addressandschedule";
            case PASS_REGISTRATION -> command = "/passRegistration";
            default -> command = "not found";
        }
        return command;
    }
}
