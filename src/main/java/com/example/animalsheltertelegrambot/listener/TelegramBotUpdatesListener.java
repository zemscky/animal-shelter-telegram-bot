package com.example.animalsheltertelegrambot.listener;

import com.example.animalsheltertelegrambot.service.ClientService;
import com.example.animalsheltertelegrambot.service.CommandService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Serves as a controller regarding processing user`s messages and commands.
 * @see ClientService
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final ClientService clientService;
    private final CommandService commandService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, ClientService clientService, CommandService commandService) {
        this.telegramBot = telegramBot;
        this.clientService = clientService;
        this.commandService = commandService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
        commandService.setTelegramBot(this.telegramBot);
    }

    /**
     * Processes incoming messages from user and sends responses.
     * @param updates new messages from user
     * @return
     * @see ClientService#sendMessage(Update)
     */
    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            clientService.sendMessage(update);
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }



}
