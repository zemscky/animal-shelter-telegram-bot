package com.example.animalsheltertelegrambot.listener;

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
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final UserService userService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      UserService userService) {
        this.telegramBot = telegramBot;
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
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
            userService.updateHandler(update);
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
