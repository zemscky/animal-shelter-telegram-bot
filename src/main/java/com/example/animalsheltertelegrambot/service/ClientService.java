package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.PhotoFile;
import com.example.animalsheltertelegrambot.repositories.AnimalRepository;
import com.example.animalsheltertelegrambot.models.Client;
import com.example.animalsheltertelegrambot.repositories.ClientRepository;
import com.example.animalsheltertelegrambot.repositories.InfoMessageRepository;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
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

            if (update.message().photo() != null) {
//                PhotoFile photoFile = new PhotoFile();

                Long chatId = update.message().chat().id();
                PhotoSize[] photo = update.message().photo();
                String f_id = Arrays.stream(photo)
                        .sorted(Comparator.comparing(PhotoSize::fileSize).reversed())
                        .findFirst()
                        .orElse(null).fileId();
                // Know photo width
                int f_width = Arrays.stream(photo)
                        .sorted(Comparator.comparing(PhotoSize::fileSize).reversed())
                        .findFirst()
                        .orElse(null).width();
                // Know photo height
                int f_height = Arrays.stream(photo)
                        .sorted(Comparator.comparing(PhotoSize::fileSize).reversed())
                        .findFirst()
                        .orElse(null).height();
                // Set photo caption
                String caption = "file_id: " + f_id + "\nwidth: " + Integer.toString(f_width) + "\nheight: " + Integer.toString(f_height);
                SendPhoto sp = new SendPhoto(chatId, f_id)
                        .caption(caption);
                commandService.testPhoto(chatId, sp);
            }

//            Long chatId = update.message().chat().id();
//            String text = update.message().text();
//            if (text.equals("/start")) {
//                InlineKeyboardMarkup keyboardMarkup = createMenuButtons();
//                this.commandService.SendPhoto(chatId, "", "images/shelter/shelter_logo.jpg");
//                this.commandService.sendResponseToCommand(chatId, text, keyboardMarkup);
//            }  else {
//                this.commandService.sendResponseToCommand(chatId, text);
//            }
        } else if (update.callbackQuery() != null) {
            this.commandService.sendCallbackQueryResponse(update.callbackQuery().id());
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

    public void saveClient(Long chatId) {
        Client client = new Client();
        client.setChatId(chatId);
        clientRepository.save(client);
    }

    public boolean clientExists(Long chatId) {
        return clientRepository.existsById(chatId);
    }
}
