package com.example.animalsheltertelegrambot.listener;

import com.example.animalsheltertelegrambot.service.ClientService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final ClientService clientService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, ClientService clientService) {
        this.telegramBot = telegramBot;
        this.clientService = clientService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
        clientService.setTelegramBot(this.telegramBot);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            Message message = update.message();
            if (update.message().text().equals("/start")) {
                getButtons(message);
                clientService.sendGreetings(update);
            } else {

            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private SendResponse getButtons(Message message) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton button = new InlineKeyboardButton("Для начала выбери раздел");
        button.callbackData("Для началa выбери раздел");
        keyboardMarkup.addRow(button);
        logger.info("Клавитаура создана");
        return telegramBot.execute(new SendMessage(message
                .chat().id(), "Отлично! Чем могу помочь?").replyMarkup(keyboardMarkup));


    }


}
