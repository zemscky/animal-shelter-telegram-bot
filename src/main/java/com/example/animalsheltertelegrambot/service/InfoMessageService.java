package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.*;
import com.example.animalsheltertelegrambot.repositories.*;
import liquibase.pro.packaged.M;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class InfoMessageService {
    private final CatInfoMessageRepository catInfoMessageRepository;
    private final DogInfoMessageRepository dogInfoMessageRepository;
    private final UserRepository userRepository;
    private final ShelterRepository shelterRepository;
    private final LocationMapRepository locationMapRepository;

    public InfoMessageService(CatInfoMessageRepository catInfoMessageRepository, DogInfoMessageRepository dogInfoMessageRepository, UserRepository userRepository, ShelterRepository shelterRepository, LocationMapRepository locationMapRepository) {
        this.catInfoMessageRepository = catInfoMessageRepository;
        this.dogInfoMessageRepository = dogInfoMessageRepository;
        this.userRepository = userRepository;
        this.shelterRepository = shelterRepository;
        this.locationMapRepository = locationMapRepository;
    }

    public void sendInfoMessage(Long chatId, String tag) {
        AnimalInfoMessage infoMessage = (AnimalInfoMessage) getRepository(chatId).findById(tag).orElseThrow(RuntimeException::new);
        MessageSender.sendMessage(chatId, tag, infoMessage.getText());
        if (tag.equals("/addressandschedule")) {
//            MessageSender.sendPhoto(chatId,
//                    "Схема проезда к нашему приюту",
//                    "images/shelter/shelter_cat_and_dog_location.jpg");
            ShelterType shelterType = userRepository.findById(chatId).orElseThrow().getShelterType();
            Shelter shelter = shelterRepository.findByShelterType(shelterType).get();
            LocationMap locationMap = locationMapRepository.findByShelterNumber(shelter.getNumber()).get();
            MessageSender.sendAddress(chatId, shelter);
            String imagePath = locationMap.getFilePath();
//            MessageSender.sendPhoto(chatId, "Схема проезда к нашему приюту", imagePath);
            MessageSender.sendPhoto(chatId, "Схема проезда к нашему приюту", "images/shelter/shelter_cat_and_dog_location.jpg");
        }
    }

    public JpaRepository<? extends Object, String> getRepository(Long chatId) {
        ShelterType shelterType = ShelterType.DOG_SHELTER;
        shelterType = userRepository.findById(chatId).orElseThrow().getShelterType();
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
