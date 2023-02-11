package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.Adopter;
import com.example.animalsheltertelegrambot.models.Report;
import com.example.animalsheltertelegrambot.models.ShelterUser;
import com.example.animalsheltertelegrambot.models.UserStatus;
import com.example.animalsheltertelegrambot.repositories.AdopterRepository;
import com.example.animalsheltertelegrambot.repositories.ReportRepository;
import com.example.animalsheltertelegrambot.repositories.ShelterUserRepository;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

@Service
public class ReportService {
    private final ShelterUserRepository shelterUserRepository;
    private final AdopterRepository adopterRepository;
    private final ReportRepository reportRepository;

    public ReportService(ShelterUserRepository shelterUserRepository, AdopterRepository adopterRepository, ReportRepository reportRepository) {
        this.shelterUserRepository = shelterUserRepository;
        this.adopterRepository = adopterRepository;
        this.reportRepository = reportRepository;
    }

    public boolean isSendReportCommand(String userMessage, Long chatId) {
        ShelterUser user = shelterUserRepository.findById(chatId).orElseThrow(RuntimeException::new);
        return userMessage.equals("/sendReport");
    }

    public void sendReportFirstStep(Long chatId) {
        ShelterUser user = shelterUserRepository.findById(chatId).orElseThrow();
        if (userIsAdopter(user.getUsername())) {
            user.setUserStatus(UserStatus.FILLING_REPORT);
            shelterUserRepository.save(user);
            MessageSender.sendMessage(chatId, "/sendReport",
                    "Отлично, приступим!\n" +
                            "В качестве отчёта Вам необходимо прислать в первом сообщении " +
                            "фото питомца, а во втором - как можно более подробное описание:\n" +
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

    public boolean userIsAdopter(String username) {
        return adopterRepository.findAdopterByUsername(username).isPresent();
    }

    public void sendReportSecondStep(Long chatId, PhotoSize[] photo) {
        ShelterUser user = shelterUserRepository.findById(chatId).orElseThrow();
        Adopter adopter = adopterRepository.findAdopterByUsername(user.getUsername()).orElseThrow();

        String photoId = Arrays.stream(photo)
                .sorted(Comparator.comparing(PhotoSize::fileSize).reversed())
                .findFirst()
                .orElse(null)
                .fileId();

        Report report = new Report();
        report.setDate(LocalDate.now());
        report.setPhotoId(photoId);
        report.setProbationPeriod(adopter.getProbationPeriod());

        reportRepository.save(report);

        Report savedReport = reportRepository.findReportByProbationPeriodAndDate(
                adopter.getProbationPeriod(),
                LocalDate.now()
        ).orElseThrow();

        SendPhoto sendPhoto = new SendPhoto(chatId, savedReport.getPhotoId());
        MessageSender.sendPhoto(sendPhoto);
        MessageSender.sendMessage(chatId,
                "Спасибо! Фото получено.\n" +
                        "Теперь пришлите, пожалуйста, письменный отчёт.");
    }

    public void sendReportThirdStep(Long chatId, String messageText) {
        ShelterUser user = shelterUserRepository.findById(chatId).orElseThrow();
        Adopter adopter = adopterRepository.findAdopterByUsername(user.getUsername()).orElseThrow();

        Report report = reportRepository.findReportByProbationPeriodAndDate(
                adopter.getProbationPeriod(),
                LocalDate.now()
        ).orElseThrow();

        report.setEntry(messageText);
        reportRepository.save(report);

        Report savedReport = reportRepository.findReportByProbationPeriodAndDate(
                adopter.getProbationPeriod(),
                LocalDate.now()
        ).orElseThrow();

        SendPhoto sendPhoto = new SendPhoto(chatId, savedReport.getPhotoId());
        SendMessage sendMessage = new SendMessage(chatId, savedReport.getEntry());

        MessageSender.sendMessage(chatId, "Спасибо! Отчёт принят, и выглядит он вот так.");
        MessageSender.sendPhoto(sendPhoto);
        MessageSender.sendMessage(sendMessage);

        user.setUserStatus(UserStatus.JUST_USING);
        shelterUserRepository.save(user);
    }
}
