package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.*;
import com.example.animalsheltertelegrambot.repositories.AdopterRepository;
import com.example.animalsheltertelegrambot.repositories.ProbationPeriodRepository;
import com.example.animalsheltertelegrambot.repositories.ReportRepository;
import com.example.animalsheltertelegrambot.repositories.ShelterUserRepository;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;

@Service
public class ReportService {
    private final ShelterUserRepository shelterUserRepository;
    private final AdopterRepository adopterRepository;
    private final ReportRepository reportRepository;
    private final ProbationPeriodRepository probationPeriodRepository;

    public ReportService(ShelterUserRepository shelterUserRepository, AdopterRepository adopterRepository, ReportRepository reportRepository, ProbationPeriodRepository probationPeriodRepository) {
        this.shelterUserRepository = shelterUserRepository;
        this.adopterRepository = adopterRepository;
        this.reportRepository = reportRepository;
        this.probationPeriodRepository = probationPeriodRepository;
    }

    public boolean isSendReportCommand(String userMessage, Long chatId) {
        ShelterUser user = shelterUserRepository.findById(chatId).orElseThrow(RuntimeException::new);
        return userMessage.equals("/sendReport");
    }

    public void sendReportFirstStep(Long chatId) {
        ShelterUser user = shelterUserRepository.findById(chatId).orElseThrow();
        if (userIsAdopter(user.getUsername())) {

            Adopter adopter = adopterRepository.findAdopterByUsername(user.getUsername()).orElseThrow();
            if (adopter.getProbationPeriod() == null) {
                ProbationPeriod probationPeriod = new ProbationPeriod();
                probationPeriod.setEnds(LocalDate.now().plusDays(30));

                adopter.setProbationPeriod(probationPeriod);
                probationPeriod.setAdopter(adopter);

                probationPeriodRepository.save(probationPeriod);
                adopterRepository.save(adopter);

            } else if (reportRepository.existsByProbationPeriodAndDate(
                    adopter.getProbationPeriod(),
                    LocalDate.now()
            )) {
                MessageSender.sendMessage(chatId, "Вы уже отправили сегодняшний отчёт. " +
                        "Обращаем Ваше внимание, что отчёт нужно заполнять один раз в день. " +
                        "Если произошла ошибка, свяжитесь, пожалуйста, с волонтёром - /volunteer. " +
                        "Или запросите обратный звонок - /callback");
                return;
            }

            user.setUserStatus(UserStatus.FILLING_REPORT);
            shelterUserRepository.save(user);

            MessageSender.sendMessage(chatId, "/sendReport",
                    "Отлично, приступим!\n" +
                            "Обращаем Ваше внимание, что отчёт нужно заполнять один раз в день каждый день " +
                            "до 21:00.\n" +
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
        ).orElse(new Report());

        report.setEntry(messageText);
        report.setProbationPeriod(adopter.getProbationPeriod());
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

    public static boolean isReportStatus(ShelterUser user) {
        UserStatus status = user.getUserStatus();
        if (status == UserStatus.FILLING_REPORT || status == UserStatus.REPORT_YOU_WANNA_TRY_AGAIN) {
            return true;
        }
        return false;
    }

    public void reportHandler(Long chatId, String userMessage, PhotoSize[] photoSize) {
        ShelterUser user = shelterUserRepository.findById(chatId).orElseThrow();
        if (user.getUserStatus() == UserStatus.REPORT_YOU_WANNA_TRY_AGAIN) {
            sendTryAgainMessage(chatId, userMessage, user);
            return;
        }

        Adopter adopter = adopterRepository.findAdopterByUsername(user.getUsername()).orElseThrow();

        Report report = this.reportRepository.findReportByProbationPeriodAndDate(
                adopter.getProbationPeriod(), LocalDate.now()).
                orElse(new Report());
        report.setDate(LocalDate.now());
        report.setProbationPeriod(adopter.getProbationPeriod());

        if (photoSize != null && report.getPhotoId() == null) {
            sendReportSecondStep(chatId, photoSize);
            return;
        }
        if (userMessage != null && report.getPhotoId() != null) {
            if (userMessage.length() < 20) {
                MessageSender.sendMessage(chatId, "incorrect length", "Информация указана не в полном объеме, попробуете еще раз?",
                        MenuService.createMenuDoubleButtons(MenuService.YES, MenuService.NO));
                user.setUserStatus(UserStatus.REPORT_YOU_WANNA_TRY_AGAIN);
                shelterUserRepository.save(user);
                return;
            }
            sendReportThirdStep(chatId, userMessage);
            return;
        }
        if (userMessage != null && report.getPhotoId() == null) {
            MessageSender.sendMessage(chatId, "Не правильный порядок действий" ,"Не правильный порядок действий. Нужно сперва загрузить фотографию. Попробуете еще раз?",
                    MenuService.createMenuDoubleButtons(MenuService.YES, MenuService.NO));

            user.setUserStatus(UserStatus.REPORT_YOU_WANNA_TRY_AGAIN);
            shelterUserRepository.save(user);
        }
    }

    public void sendTryAgainMessage(Long chatId, String userMessage, ShelterUser user) {
        if (userMessage.equalsIgnoreCase("ДА")) {
            user.setUserStatus(UserStatus.FILLING_REPORT);
            shelterUserRepository.save(user);
            MessageSender.sendMessage(chatId, "Хорошо, тогда попробуйте еще раз");
        } else {
            user.setUserStatus(UserStatus.JUST_USING);
            shelterUserRepository.save(user);
            MessageSender.sendMessage(chatId, "Нет? Тогда вернёмся к этому вопросу позже. Если нужно вернуться в начальное меню, используйте /start");
        }

    }

}
