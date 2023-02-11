package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.repositories.AnimalRepository;
import com.example.animalsheltertelegrambot.repositories.UserRepository;
import com.example.animalsheltertelegrambot.repositories.InfoMessageRepository;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.animalsheltertelegrambot.constants.Constants.*;

@Service
public class MessageService {

    private final Logger logger = LoggerFactory.getLogger(MessageService.class);

    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;
    private final InfoMessageRepository messageRepository;
    private final CommandService commandService;

    public MessageService(UserRepository userRepository, AnimalRepository animalRepository, InfoMessageRepository messageRepository, CommandService commandService) {
        this.userRepository = userRepository;
        this.animalRepository = animalRepository;
        this.messageRepository = messageRepository;
        this.commandService = commandService;
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
            }  else {
                this.commandService.sendResponseToCommand(chatId, text);
            }
        } else if (update.callbackQuery() != null) {
            this.commandService.sendCallbackQueryResponse(update.callbackQuery().id());
            Long chatId = update.callbackQuery().message().chat().id();
            String text = update.callbackQuery().data();
            sendSectionMenu(chatId, text);
        }
    }

    private InlineKeyboardMarkup createMenuButtons() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton dogMenu = new InlineKeyboardButton(DOG_SHELTER_CMD);
        InlineKeyboardButton catMenu = new InlineKeyboardButton(CAT_SHELTER_CMD);

        dogMenu.callbackData(dogMenu.text());
        catMenu.callbackData(catMenu.text());

        keyboardMarkup.addRow(dogMenu);
        keyboardMarkup.addRow(catMenu);

        return keyboardMarkup;
    }

    private InlineKeyboardMarkup createMenuButtonsDog() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton generalButton = new InlineKeyboardButton(GENERAL_INFO);
        InlineKeyboardButton dogsButton = new InlineKeyboardButton(DOG_INFO);
        InlineKeyboardButton sendReportButton = new InlineKeyboardButton(SEND_REPORT_DOG);
        InlineKeyboardButton callbackRequestButton = new InlineKeyboardButton(CALLBACK);
        InlineKeyboardButton volunteerButton = new InlineKeyboardButton(VOLUNTEER);
        InlineKeyboardButton backStartMenu = new InlineKeyboardButton(BACK_TO_CHOOSE_ANIMAL);

        generalButton.callbackData(generalButton.text());
        dogsButton.callbackData(dogsButton.text());
        sendReportButton.callbackData(sendReportButton.text());
        callbackRequestButton.callbackData(callbackRequestButton.text());
        volunteerButton.callbackData(volunteerButton.text());
        backStartMenu.callbackData(backStartMenu.text());

        keyboardMarkup.addRow(generalButton);
        keyboardMarkup.addRow(dogsButton);
        keyboardMarkup.addRow(sendReportButton);
        keyboardMarkup.addRow(callbackRequestButton);
        keyboardMarkup.addRow(volunteerButton);
        keyboardMarkup.addRow(backStartMenu);

        return keyboardMarkup;
    }
    private InlineKeyboardMarkup createMenuButtonsCat() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton generalButton = new InlineKeyboardButton(GENERAL_INFO);
        InlineKeyboardButton catButton = new InlineKeyboardButton(CAT_INFO);
        InlineKeyboardButton sendReportButton = new InlineKeyboardButton(SEND_REPORT_CAT);
        InlineKeyboardButton callbackRequestButton = new InlineKeyboardButton(CALLBACK);
        InlineKeyboardButton volunteerButton = new InlineKeyboardButton(VOLUNTEER);
        InlineKeyboardButton backStartMenu = new InlineKeyboardButton(BACK_TO_CHOOSE_ANIMAL);

        generalButton.callbackData(generalButton.text());
        catButton.callbackData(catButton.text());
        sendReportButton.callbackData(sendReportButton.text());
        callbackRequestButton.callbackData(callbackRequestButton.text());
        volunteerButton.callbackData(volunteerButton.text());
        backStartMenu.callbackData(backStartMenu.text());

        keyboardMarkup.addRow(generalButton);
        keyboardMarkup.addRow(catButton);
        keyboardMarkup.addRow(sendReportButton);
        keyboardMarkup.addRow(callbackRequestButton);
        keyboardMarkup.addRow(volunteerButton);
        keyboardMarkup.addRow(backStartMenu);

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
    private InlineKeyboardMarkup createMenuButtonsDogInfo() {
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
    private InlineKeyboardMarkup createMenuButtonsCatInfo() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton documentsForAdoption = new InlineKeyboardButton(DOCUMENTS_FOR_ADOPTION);
        InlineKeyboardButton actionFailure = new InlineKeyboardButton(ACTION_FAILURE);

        documentsForAdoption.callbackData(documentsForAdoption.text());
        actionFailure.callbackData(actionFailure.text());

        inlineKeyboardMarkup.addRow(documentsForAdoption);
        inlineKeyboardMarkup.addRow(actionFailure);

        return inlineKeyboardMarkup;
    }

    public void sendSectionMenu(Long chatId, String text) {
        switch (text) {
            case DOG_SHELTER_CMD -> {
                InlineKeyboardMarkup keyboardMarkup = createMenuButtonsDog();
                this.commandService.sendResponseToCommand(chatId,"/description", keyboardMarkup);
            }
            case CAT_SHELTER_CMD -> {
                InlineKeyboardMarkup keyboardMarkup = createMenuButtonsCat();
                this.commandService.sendResponseToCommand(chatId,"/description", keyboardMarkup);
            }
            case GENERAL_INFO -> {
                InlineKeyboardMarkup keyboardMarkup = createMenuButtonsGeneralInfo();
                this.commandService.sendResponseToCommand(chatId, "/description", keyboardMarkup);
            }
            case DOG_INFO -> {
                InlineKeyboardMarkup inlineKeyboardButton = createMenuButtonsDogInfo();
                this.commandService.sendResponseToCommand(chatId, "/animalmenu", inlineKeyboardButton);
            }
            case CAT_INFO -> {
                InlineKeyboardMarkup inlineKeyboardButton = createMenuButtonsCatInfo();
                this.commandService.sendResponseToCommand(chatId, "/animalmenu", inlineKeyboardButton);
            }
            case BACK_TO_CHOOSE_ANIMAL ->{
                InlineKeyboardMarkup keyboardMarkup = createMenuButtons();
                this.commandService.sendResponseToCommand(chatId,"/description", keyboardMarkup);
            }
            case SEND_REPORT_DOG -> this.commandService.sendResponseToCommand(chatId, "/sendreportmenu");
            case ADDRESS_SCHEDULE -> this.commandService.sendResponseToCommand(chatId, "/addressandschedule");
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
