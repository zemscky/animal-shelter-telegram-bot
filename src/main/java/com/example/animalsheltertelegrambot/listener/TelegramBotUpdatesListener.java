package com.example.animalsheltertelegrambot.listener;

import com.example.animalsheltertelegrambot.service.BotExecutionService;
import com.example.animalsheltertelegrambot.service.HeadService;
import com.example.animalsheltertelegrambot.service.UserService;
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
 * @see UserService
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final UserService userService;
    private final CommandService commandService;
    private final HeadService headService;
    private final BotExecutionService botExecutionService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, UserService userService, CommandService commandService, HeadService headService, BotExecutionService botExecutionService) {
        this.telegramBot = telegramBot;
        this.userService = userService;
        this.commandService = commandService;
        this.headService = headService;
        this.botExecutionService = botExecutionService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
        commandService.setTelegramBot(this.telegramBot);
        headService.setTelegramBot(this.telegramBot);
        botExecutionService.setTelegramBot(this.telegramBot);
    }

    /**
     * Processes incoming messages from user and sends responses.
     * @param updates new messages from user
     * @return
     * @see UserService#sendMessage(Update)
     */
    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            if (update.message() != null) {
                headService.analyzeMessageAndRedirect(update.message());
            } else if (update.callbackQuery() != null) {
                headService.analyzeCallbackQueryAndRedirect(update.callbackQuery());
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }



}
