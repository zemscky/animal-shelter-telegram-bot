package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.LocationMap;
import com.example.animalsheltertelegrambot.models.Shelter;
import com.example.animalsheltertelegrambot.repositories.AnimalRepository;
import com.example.animalsheltertelegrambot.repositories.LocationMapRepository;
import com.example.animalsheltertelegrambot.repositories.ShelterRepository;
import com.pengrad.telegrambot.TelegramBot;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class FileService {

    @Value("${path.to.shelters.folder}")
    private String shelterDir;

    private final Logger logger = LoggerFactory.getLogger(CommandService.class);

    private final AnimalRepository animalRepository;
    private final ShelterRepository shelterRepository;

    private final LocationMapRepository locationMapRepository;

    private TelegramBot telegramBot;

    public FileService(AnimalRepository animalRepository, ShelterRepository shelterRepository, LocationMapRepository locationMapRepository, TelegramBot telegramBot) {
        this.animalRepository = animalRepository;
        this.shelterRepository = shelterRepository;
        this.locationMapRepository = locationMapRepository;
        this.telegramBot = telegramBot;
    }

    public void uploadPhotoShelter(String fileId, String fileName, long fileSize) throws IOException {

        String shelterNumber = getShelterNumber(fileName);

        URL url = new URL("https://api.telegram.org/bot" + telegramBot.getToken() + "/getFile?file_id=" + fileId);

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String getFileResponse = br.readLine();

        JSONObject jsonResult = new JSONObject(getFileResponse);
        JSONObject path = jsonResult.getJSONObject("result");
        String file_path = path.getString("file_path");

        Path filePath = Path.of(shelterDir + "/" + fileName + "." + getExtension(file_path));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = new URL("https://api.telegram.org/file/bot" +
                telegramBot.getToken() + "/" + file_path).openStream();
        OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
        BufferedInputStream bis = new BufferedInputStream(is, 1024);
        BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }

        br.close();

        // Чтобы записать в базу фото схемы проезда  - надо нажать на скрепочку в телеграме,
        // выбрать файл, в сообщении написать имя файла (или в подписи окошки отправки)
        // строго в формате 'shelter-map-1' где 1 - номер приюта
        // Проблема - если приюта с таким именем нет в базе - то ошибка, пока не устранил,
        // выскакивает в следующей за этой строчкой
        Shelter shelter = shelterRepository.getReferenceById(shelterNumber);
        if (fileName.contains("-map-")) {
            LocationMap locationMap = findLocationMap(shelterNumber);
            locationMap.setShelter(shelter);
            locationMap.setNumber(shelterNumber);
            locationMap.setFilePath(filePath.toString());
            locationMap.setFileSize(fileSize);

            locationMapRepository.save(locationMap);
        }
    }
    // Надо id с LocationMap и с Shelter удалить, а также удалить лишние поля в LocationMap

    public LocationMap findLocationMap(String shelterNumber) {
        logger.info("Was invoked method to get the LocationMap of the shelter with number = " + shelterNumber);
        return locationMapRepository.findByShelterNumber(shelterNumber).orElse(new LocationMap());
    }

    private String getExtension(String file_path) {
        logger.info("A method was called to extract the file extension");
        String fileExtension = file_path.substring(file_path.lastIndexOf(".") + 1);
        logger.debug("File extension '{}'", fileExtension);
        return fileExtension;
    }

    private String getShelterNumber(String fileName) {
        String[] parts = fileName.split("\\-");
        return parts[2];
    }

}
