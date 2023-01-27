package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.listener.TelegramBotUpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TelegramBotService {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    public void sendGreetings(Update update) {
//        logger.info("Sending the greeting message");
//        long chatId = update.message().chat().id();
//        SendMessage message = new SendMessage(chatId,
//                "Hi! I`m...");
//        SendResponse response = telegramBot.execute(message);
//        if (!response.isOk()) {
//            logger.error("Could not send the greeting message! " +
//                    "Error code: {}", response.errorCode());
//        }
    }
}
