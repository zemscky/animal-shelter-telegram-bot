package com.example.animalsheltertelegrambot.listener;

import com.example.animalsheltertelegrambot.service.ClientService;
import com.example.animalsheltertelegrambot.service.CommandService;
import com.example.animalsheltertelegrambot.service.MessageSender;
import com.example.animalsheltertelegrambot.service.UserService;
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
    private final UserService userService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, ClientService clientService, CommandService commandService, UserService userService) {
        this.telegramBot = telegramBot;
        this.clientService = clientService;
        this.commandService = commandService;
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
        commandService.setTelegramBot(this.telegramBot);
        MessageSender.setTelegramBot(this.telegramBot);
    }

    /**
     * Processes incoming messages from user and sends responses.
     * @param updates new messages from user
     * @return
     */
    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            userService.updateHandle(update);
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }



}
