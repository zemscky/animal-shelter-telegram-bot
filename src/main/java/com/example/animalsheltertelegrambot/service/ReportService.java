package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.ShelterUser;
import com.example.animalsheltertelegrambot.repositories.AdopterRepository;
import com.example.animalsheltertelegrambot.repositories.ShelterUserRepository;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
    private final ShelterUserRepository shelterUserRepository;
    private final AdopterRepository adopterRepository;

    public ReportService(ShelterUserRepository shelterUserRepository, AdopterRepository adopterRepository) {
        this.shelterUserRepository = shelterUserRepository;
        this.adopterRepository = adopterRepository;
    }

    public boolean isSendReportCommand(String userMessage, Long chatId) {
        ShelterUser user = shelterUserRepository.findById(chatId).orElseThrow(RuntimeException::new);
        return userMessage.equals("/sendReport");
    }

    public void sendReportMessage(Long chatId) {
        if (userIsAdopter(chatId)) {

            MessageSender.sendMessage(chatId, "/sendReport",
                    "Отлично, приступим!\n" +
                            "В качестве отчёта Вам необходимо прислать " +
                            "фото питомца, а также как можно более подробное описание:\n" +
                            "- рациона животного,\n" +
                            "- общего самочувствия и привыкания к новому месту,\n" +
                            "- изменений в поведении: отказ от старых привычек, приобретение новых.\n" +
                            "Сначала пришлите, пожалуйста, фото.");
        } else {
            MessageSender.sendMessage(chatId, "К сожалению, Вы не являетесь усыновителем животного. " +
                    "Пожалуйста, приезжайте в приют с необходимыми документами и выберите питомца. " +
                    "Волонтёр зарегистрирует Вас в системе и Вы сможете отправлять отчёты. " +
                    "Если произошла ошибка, свяжитесь, пожалуйста, с волонтёром - /volunteer. " +
                    "Или запросите обратный звонок - /callback");
        }
    }

    public boolean userIsAdopter(Long chatId) {
        return adopterRepository.existsById(chatId);
    }
}
