package com.example.animalsheltertelegrambot.listener;

import com.example.animalsheltertelegrambot.service.ClientService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
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

//    /start
//    /description
//    /callback
//    @Override
//    public int process(List<Update> updates) {
//        updates.forEach(update -> {
//            logger.info("Processing update: {}", update);
//            clientService.sendMessage(update);
//        });
//        return UpdatesListener.CONFIRMED_UPDATES_ALL;
//    }

    @Override
    public int process(List<Update> list) {
        list.stream()
                .filter(update -> update.message() != null)
                .filter(update -> update.message().text() != null)
                .forEach(this::processUpdate);
        return CONFIRMED_UPDATES_ALL;
    }

    private void processUpdate(Update update) {
        String userMessage = update.message().text();
        Long chatId = update.message().chat().id();
        if (userMessage.toLowerCase().contains("/contact")) {
            logger.info("Processing update: {}", update);
            clientService.sendContacts(update);
        } else {
            logger.info("Processing update: {}", update);
            clientService.sendMessage(update);
        }
    }
}
