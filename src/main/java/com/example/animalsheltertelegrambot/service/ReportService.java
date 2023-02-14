package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.*;
import com.example.animalsheltertelegrambot.repositories.*;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class ReportService {

//    Logger logger = LoggerFactory.getLogger(ReportService.class);

    private final ShelterUserRepository shelterUserRepository;
    private final AdopterRepository adopterRepository;
    private final ReportRepository reportRepository;
    private final ProbationPeriodRepository probationPeriodRepository;
    private final AnimalRepository animalRepository;

    public ReportService(ShelterUserRepository shelterUserRepository, AdopterRepository adopterRepository, ReportRepository reportRepository, ProbationPeriodRepository probationPeriodRepository, AnimalRepository animalRepository) {
        this.shelterUserRepository = shelterUserRepository;
        this.adopterRepository = adopterRepository;
        this.reportRepository = reportRepository;
        this.probationPeriodRepository = probationPeriodRepository;
        this.animalRepository = animalRepository;
    }

    public boolean isSendReportCommand(String userMessage) {
        return userMessage.equals("/sendReport");
    }

    public void sendReportFirstStep(Long chatId) {
        ShelterUser user = shelterUserRepository.findById(chatId).orElseThrow();
        if (userIsAdopter(user.getUsername())) {

            Adopter adopter = adopterRepository.findAdopterByUsername(
                    user.getUsername()).orElseThrow();
            if (adopter.getProbationPeriods().isEmpty() ||
                    adopter.getAnimals().isEmpty()) {

                MessageSender.sendMessage(chatId, "Что-то пошло не так! За Вами " +
                        "не закреплён питомец и/или не назначен испытательный срок!" +
                        "Свяжитесь, пожалуйста, с волонтёром - /volunteer. " +
                        "Или запросите обратный звонок - /callback. Приносим свои " +
                        "извинения.");
                return;

//                List<ProbationPeriod> probPeriodsList = new ArrayList<>();
//                ProbationPeriod probationPeriod = new ProbationPeriod();
//                probationPeriod.setEnds(LocalDate.now().plusDays(30));
//                probationPeriod.setAdopter(adopter);
//
//                probPeriodsList.add(probationPeriod);
//                adopter.setProbationPeriods(probPeriodsList);
//
//                probationPeriodRepository.save(probationPeriod);
//                adopterRepository.save(adopter);

            }

            List<Animal> chosenSheltersAnimals = new ArrayList<>();
            for (Animal a : adopter.getAnimals()) {
                if (a.getShelter().getShelterType() == user.getShelterType()) {
                    chosenSheltersAnimals.add(a);
                }
            }
            if (chosenSheltersAnimals.isEmpty()) {
                MessageSender.sendMessage(chatId, "Я вижу, что питомец(ы), которых Вы забрали, - " +
                        "не из выбранного Вами сейчас приюта. Пожалуйста, нажмите /start, " +
                        "чтобы выбрать нужный приют. " +
                        "Если произошла ошибка, свяжитесь, пожалуйста, с волонтёром - /volunteer. " +
                        "Или запросите обратный звонок - /callback");
                return;
            }

//            for (ProbationPeriod p : adopter.getProbationPeriods()) {
//                if (reportRepository.existsByProbationPeriodAndDate(
//                        p, LocalDate.now())) {
//                    MessageSender.sendMessage(chatId, "Вы уже отправили сегодняшний отчёт. " +
//                            "Обращаем Ваше внимание, что отчёт нужно заполнять один раз в день. " +
//                            "Если произошла ошибка, свяжитесь, пожалуйста, с волонтёром - /volunteer. " +
//                            "Или запросите обратный звонок - /callback");
//                    return;
//                }
//            }

            List<String> buttonNames = new ArrayList<>();
            for (Animal a : adopter.getAnimals()) {
                String s = "№" + a.getId() + " " + a.getName();
                buttonNames.add(s);
            }

            InlineKeyboardMarkup keyboardMarkup = MenuService.createMenuDoubleButtons(
                    buttonNames.toArray(new String[0])
            );

            MessageSender.sendMessage(chatId, "/sendReportFirstStep",
                    "Отлично, приступим!\n" +
                            "Обращаем Ваше внимание, что отчёт нужно заполнять только " +
                            "один раз в день каждый день до 21:00.\n" +
                            "Следуя инструкциям бота, в качестве отчёта Вам необходимо " +
                            "прислать в первом сообщении фото питомца, а во втором - " +
                            "как можно более подробное описание:\n" +
                            "- рациона животного,\n" +
                            "- общего самочувствия и привыкания к новому месту,\n" +
                            "- изменений в поведении: отказ от старых привычек, приобретение " +
                            "новых.\n" +
                            "Сначала выберите, пожалуйста, питомца, о котором собираетесь " +
                            "отправить отчёт.",
                    keyboardMarkup);

            user.setUserStatus(UserStatus.CHOOSING_PET_FOR_REPORT);
            shelterUserRepository.save(user);
        } else {
            MessageSender.sendMessage(chatId, "/sendReportFirstStep",
                    "К сожалению, Вы не являетесь усыновителем животного. " +
                    "Пожалуйста, приезжайте в приют с необходимыми документами и выберите питомца. " +
                    "Волонтёр зарегистрирует Вас в системе и Вы сможете отправлять отчёты. " +
                    "Если произошла ошибка, свяжитесь, пожалуйста, с волонтёром - /volunteer. " +
                    "Или запросите обратный звонок - /callback");
        }
    }

    public boolean userIsAdopter(String username) {
        return adopterRepository.findAdopterByUsername(username).isPresent();
    }

    public void sendReportSecondStep(Long chatId, String callbackData) {
        ShelterUser user = shelterUserRepository.findById(chatId).orElseThrow();

        if (!callbackData.startsWith("№")) {
            user.setUserStatus(UserStatus.JUST_USING);
            MessageSender.sendMessage(chatId, "/sendReportSecondStep",
                    "Вы не выбрали животное. Чтобы попробовать ещё раз, " +
                            "пожалуйста, нажмите на кнопку 'Отправить отчёт' " +
                            "выше или нажмите сюда -> /start");
            return;
        }

        String animalIdString = callbackData.substring(1,
                callbackData.indexOf(" "));
        Long animalId = Long.parseLong(animalIdString);

        Adopter adopter = adopterRepository.findAdopterByUsername(user.getUsername()).orElseThrow();

        ProbationPeriod probPeriod = probationPeriodRepository.findByAnimal_Id(animalId);

        Report report = new Report();
        report.setDate(LocalDate.now());
        report.setProbationPeriod(probPeriod);
        Report savedReport = reportRepository.save(report);

        adopter.setCurrentReportId(savedReport.getId());
        adopterRepository.save(adopter);

        user.setUserStatus(UserStatus.FILLING_REPORT);
        shelterUserRepository.save(user);

        Animal animal = animalRepository.findById(animalId).orElseThrow();

        MessageSender.sendMessage(chatId, "/sendReportSecondStep",
                "Вы выбрали " + animal.getName() +
                ". Пришлите, пожалуйста, её/его фото.");
    }

    public void sendReportThirdStep(Long chatId, PhotoSize[] photo) {
        ShelterUser user = shelterUserRepository.findById(chatId).orElseThrow();
        Adopter adopter = adopterRepository.findAdopterByUsername(user.getUsername()).orElseThrow();

        String photoId = Arrays.stream(photo)
                .sorted(Comparator.comparing(PhotoSize::fileSize).reversed())
                .findFirst()
                .orElse(null)
                .fileId();

        Report report = reportRepository.findById(adopter.getCurrentReportId()).orElseThrow();
        report.setPhotoId(photoId);
        Report savedReport = reportRepository.save(report);

        SendPhoto sendPhoto = new SendPhoto(chatId, savedReport.getPhotoId());
        MessageSender.sendPhoto(sendPhoto);
        MessageSender.sendMessage(chatId,
                "/sendReportThirdStep",
                "Спасибо! Фото получено.\n" +
                        "Теперь пришлите, пожалуйста, письменный отчёт.");
    }

    public void sendReportFourthStep(Long chatId, String messageText) {
        ShelterUser user = shelterUserRepository.findById(chatId).orElseThrow();
        Adopter adopter = adopterRepository.findAdopterByUsername(user.getUsername()).orElseThrow();

        Report report = reportRepository.findById(adopter.getCurrentReportId()).orElseThrow();
        report.setEntry(messageText);
        Report savedReport = reportRepository.save(report);

        SendPhoto sendPhoto = new SendPhoto(chatId, savedReport.getPhotoId());
        SendMessage sendMessage = new SendMessage(chatId, savedReport.getEntry());

        MessageSender.sendMessage(chatId,
                "/sendReportFourthStep",
                "Спасибо! Отчёт принят, и выглядит он вот так.");
        MessageSender.sendPhoto(sendPhoto);
        MessageSender.sendMessage(sendMessage);

        user.setUserStatus(UserStatus.JUST_USING);
        shelterUserRepository.save(user);

        adopter.setCurrentReportId(null);
        adopterRepository.save(adopter);
    }

    public static boolean isReportStatus(ShelterUser user) {
        UserStatus status = user.getUserStatus();
        if (status == UserStatus.FILLING_REPORT ||
                status == UserStatus.REPORT_YOU_WANNA_TRY_AGAIN) {
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

        Report report = this.reportRepository.findById(adopter.getCurrentReportId()).orElseThrow();

        if (photoSize != null && report.getPhotoId() == null) {
            sendReportThirdStep(chatId, photoSize);
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
            sendReportFourthStep(chatId, userMessage);
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
