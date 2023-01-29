package com.example.animalsheltertelegrambot.listener;

import com.example.animalsheltertelegrambot.service.ClientService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
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

            switch (update.message().text()) {
                case "/start" -> clientService.sendGreetings(update);
                case "/description" -> clientService.sendShelterDescription(update);
                case "/callback" -> clientService.sendCallBackMessage(update);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
