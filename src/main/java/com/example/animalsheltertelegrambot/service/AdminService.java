package com.example.animalsheltertelegrambot.service;

import com.pengrad.telegrambot.model.PhotoSize;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    public static void sendAdminMenu(Long chatId) {
        MessageSender.sendMessage(chatId, "send admin menu","Выберите, для какого приюта загрузить карту проезда.\n" +
                "\n" +
                "Отправьте соответствующую картинку со схемой проезда и подпишите отправляемое сообщение с фотографией следующим образом:\n" +
                "\n" +
                "если схема для приюта кошек, подпишите сообщение:\n" +
                "shelter-map-1\n"+
                "если схема для приюта собак, подпишите сообщение:\n" +
                "shelter-map-2");
    }

}
