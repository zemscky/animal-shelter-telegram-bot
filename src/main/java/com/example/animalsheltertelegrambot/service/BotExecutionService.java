package com.example.animalsheltertelegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BotExecutionService {

    private final Logger logger = LoggerFactory.getLogger(BotExecutionService.class);

    private TelegramBot telegramBot;

    public BotExecutionService() {
    }

    public void setTelegramBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendMessage(SendMessage sendMessage) {
        SendResponse response = telegramBot.execute(sendMessage);
        if (!response.isOk()) {
            logger.error("Could not send the message! " +
                    "Error code: {}", response.errorCode());
        }
    }
}
