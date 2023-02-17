package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.*;
import com.example.animalsheltertelegrambot.repositories.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Transactional
@Service
public class AdminService {
    private final AdopterRepository adopterRepository;
    private final ShelterUserRepository userRepository;
    private final AnimalRepository animalRepository;
    private final ShelterRepository shelterRepository;
    private final ProbationPeriodRepository probationPeriodRepository;

    public AdminService(AdopterRepository repository, ShelterUserRepository userRepository, AnimalRepository animalRepository, ShelterRepository shelterRepository, ProbationPeriodRepository probationPeriodRepository) {
        this.adopterRepository = repository;
        this.userRepository = userRepository;
        this.animalRepository = animalRepository;
        this.shelterRepository = shelterRepository;
        this.probationPeriodRepository = probationPeriodRepository;
    }

    public void sendRoadMapUpdateInstruction(Long chatId) {
        MessageSender.sendMessage(chatId, "map road update instruction","Выберите, для какого приюта загрузить карту проезда.\n" +
                "\n" +
                "Отправьте соответствующую картинку со схемой проезда и подпишите отправляемое сообщение с фотографией следующим образом:\n" +
                "\n" +
                "если схема для приюта кошек, подпишите сообщение:\n" +
                "shelter-map-1\n"+
                "если схема для приюта собак, подпишите сообщение:\n" +
                "shelter-map-2");
    }

    public void sendAdminMenu(Long chatId) {
        MessageSender.sendMessage(chatId, "admin menu", "Привет, админ, выбери меню:", MenuService.createMenuDoubleButtons("update road map", "add adopter username"));
    }

    public void sendAdopterUsernameInputMessage(Long chatId) {
        MessageSender.sendMessage(chatId, "add Username To Admin Database", "Введи username, добавлю нового усыновителя");
        ShelterUser user = userRepository.findById(chatId).orElseThrow();
        user.setUserStatus(UserStatus.SENDING_ADOPTER_USERNAME);
        userRepository.save(user);
    }

    public void addUsernameToAdopterDatabase(Long chatId, String username) {
        ShelterUser user = userRepository.findById(chatId).orElseThrow();
        user.setUserStatus(UserStatus.JUST_USING);
        userRepository.save(user);

        Adopter adopter = new Adopter();
        adopter.setUsername(username);
        adopter.setAddress("test address");
        adopter.setName(username);
        adopter.setAge(25);
        adopter.setChatId(chatId);
        adopter.setTelephoneNumber("test number");

        adopterRepository.save(adopter);
        MessageSender.sendMessage(chatId, "adopter добавлен в базу данных усыновителей");

        addAnimalAndProbationPeriod(chatId, adopter);
        MessageSender.sendMessage(chatId, "animal добавлен в базу данных животных");
        MessageSender.sendMessage(chatId, "probation period добавлен в базу данных");

        MenuService.sendChoiceShelterMenu(chatId);
    }

    private void addAnimalAndProbationPeriod(Long chatId, Adopter adopter) {
        Animal animal = new Animal();
        int random = (int) (Math.random() * 15);
        animal.setAge(random);
        animal.setName("name" + random);
        animal.setAdopter(adopter);
        animal.setClient(adopter);
        animal.setColor("color" + random);


        Shelter shelter = shelterRepository.findById("1").orElseThrow();
        animal.setShelter(shelter);

        animal.setSpecialNeeds("specialNeeds" + random);
        animal.setUniqueCharacteristics("UniqueCharacteristics" + random);
        animal.setSpecies("species" + random);

        this.animalRepository.save(animal);

        ProbationPeriod probationPeriod = new ProbationPeriod();
        probationPeriod.setAdopter(adopter);
        probationPeriod.setEnds(LocalDate.now().plusDays(30L));
        probationPeriod.setAnimal(animal);
        probationPeriod.setVolunteersComment("testComment" + random);
        this.probationPeriodRepository.save(probationPeriod);
    }

}
