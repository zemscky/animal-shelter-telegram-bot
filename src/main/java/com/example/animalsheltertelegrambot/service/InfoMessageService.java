package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.*;
import com.example.animalsheltertelegrambot.repositories.CatInfoMessageRepository;
import com.example.animalsheltertelegrambot.repositories.DogInfoMessageRepository;
import com.example.animalsheltertelegrambot.repositories.ShelterUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class InfoMessageService {
    private final CatInfoMessageRepository catInfoMessageRepository;
    private final DogInfoMessageRepository dogInfoMessageRepository;
    private final ShelterUserRepository shelterUserRepository;

    public InfoMessageService(CatInfoMessageRepository catInfoMessageRepository, DogInfoMessageRepository dogInfoMessageRepository, ShelterUserRepository shelterUserRepository) {
        this.catInfoMessageRepository = catInfoMessageRepository;
        this.dogInfoMessageRepository = dogInfoMessageRepository;
        this.shelterUserRepository = shelterUserRepository;
    }

    public void sendInfoMessage(Long chatId, String tag) {
        AnimalInfoMessage infoMessage = (AnimalInfoMessage) getRepository(chatId).findById(tag).orElseThrow(RuntimeException::new);
        MessageSender.sendMessage(chatId, tag, infoMessage.getText());
        if (tag.equals("/addressandschedule")) {
            MessageSender.sendPhoto(chatId,
                    "Схема проезда к нашему приюту",
                    "images/shelter/shelter_cat_and_dog_location.jpg");
        }
    }

    public JpaRepository<? extends Object, String> getRepository(Long chatId) {
        ShelterType shelterType = ShelterType.DOG_SHELTER;
        shelterType = shelterUserRepository.findById(chatId).orElseThrow().getShelterType();
        if (shelterType == null) {
            throw new RuntimeException();
        }
        if (shelterType.equals(ShelterType.CAT_SHELTER)) {
            return catInfoMessageRepository;
        } else if (shelterType.equals(ShelterType.DOG_SHELTER)) {
            return dogInfoMessageRepository;
        } else {
            throw new RuntimeException();
        }
    }

    public boolean isInfo(String userMessage, Long chatId) {
        return getRepository(chatId).findById(userMessage).isPresent();
    }
}
