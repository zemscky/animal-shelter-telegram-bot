package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.*;
import com.example.animalsheltertelegrambot.repositories.*;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Transactional
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
            }

            if (adopter.getChatId() == null || !adopter.getChatId().equals(chatId)) {
                adopter.setChatId(chatId);
                adopterRepository.save(adopter);
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

            List<Animal> unexpired = new ArrayList<>();
            for (Animal a : chosenSheltersAnimals) {
                if (!a.getProbationPeriod().getEnds().isBefore(LocalDate.now())) {
                    unexpired.add(a);
                }
            }
            if (unexpired.isEmpty()) {
                MessageSender.sendMessage(chatId, "Ваши испытательный(е) срок(и) " +
                        "окончены! Вам больше не нужно отправлять отчёты. " +
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
            for (Animal a : unexpired) {
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
            resetUsersShelterTypeAndCancel(chatId, user);
            return;
        }

        String animalIdString = callbackData.substring(1,
                callbackData.indexOf(" "));
        Long animalId = Long.parseLong(animalIdString);

        Adopter adopter = adopterRepository.findAdopterByUsername(user.getUsername()).orElseThrow();
        ProbationPeriod probPeriod = probationPeriodRepository.findByAnimal_Id(animalId);

        Report report;
        if (reportRepository.existsByProbationPeriodAndDate(
                probPeriod, LocalDate.now())) {
            report = reportRepository.findReportByProbationPeriodAndDate(
                    probPeriod, LocalDate.now()).orElseThrow();
            if (report.getEntry() != null && report.getPhotoId() != null) {
                MessageSender.sendMessage(chatId, "Вы уже отправили сегодняшний " +
                        "отчёт по выбранному питомцу. " +
                        "Обращаем Ваше внимание, что отчёт нужно заполнять один раз в день. " +
                        "Если произошла ошибка, свяжитесь, пожалуйста, с волонтёром - /volunteer. " +
                        "Или запросите обратный звонок - /callback");
                user.setUserStatus(UserStatus.JUST_USING);
                shelterUserRepository.save(user);
                return;
            }
            if (report.getPhotoId() != null && report.getEntry() == null) {
                MessageSender.sendMessage(chatId, "Фото для данного " +
                        "отчёта уже было получено.\n" +
                        "Напишите, пожалуйста, письменный отчёт. " +
                        "В нём должно быть как можно более подробное описание:\n" +
                        "- рациона животного,\n" +
                        "- общего самочувствия и привыкания к новому месту,\n" +
                        "- изменений в поведении: отказ от старых привычек, приобретение " +
                        "новых.");
                user.setUserStatus(UserStatus.FILLING_REPORT);
                shelterUserRepository.save(user);
                return;
            }
        } else {
            report = new Report();
            report.setDate(LocalDate.now());
            report.setProbationPeriod(probPeriod);
        }
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

    public void resetUsersShelterTypeAndCancel(Long chatId, ShelterUser user) {
        user.setUserStatus(UserStatus.JUST_USING);
        shelterUserRepository.save(user);
        MessageSender.sendMessage(chatId, "/sendReportSecondStep",
                "Вы не выбрали животное. Чтобы попробовать ещё раз, " +
                        "пожалуйста, начните сначала, нажав на кнопку " +
                        "'Отправить отчёт' " +
                        "выше или сюда -> /start");
    }

    public static boolean isReportStatus(ShelterUser user) {
        UserStatus status = user.getUserStatus();
        if (status == UserStatus.FILLING_REPORT ||
                status == UserStatus.REPORT_YOU_WANNA_TRY_AGAIN ||
                status == UserStatus.CHOOSING_PET_FOR_REPORT) {
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
        if (user.getUserStatus() == UserStatus.CHOOSING_PET_FOR_REPORT) {
            resetUsersShelterTypeAndCancel(chatId, user);
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
        if (photoSize != null && report.getPhotoId() != null) {
            MessageSender.sendMessage(chatId, "Фото уже есть",
                    "Фото для данного отчёта уже было получено. Необходимо " +
                            "отправить письменный отчёт. Попробуете отправить письменный " +
                            "отчёт еще раз?",
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

    @Scheduled(cron = "0 00 10 * * *") // каждый день в 10 утра
    public void sendDailyReportReminder() {
        Collection<Adopter> allAdopters = adopterRepository.findAllWithExistingChatId();
        for (Adopter adopter : allAdopters) {
            if (adopter.getProbationPeriods().stream().allMatch(
                    probationPeriod -> probationPeriod.getEnds().isBefore(
                            LocalDate.now()
                    )
            )) {
                allAdopters.remove(adopter);
            }
        }
        allAdopters.forEach(adopter -> {
            MessageSender.sendMessage(adopter.getChatId(),
                    "/dailyReportNotification",
                    "Доброе утро! Напоминаем, что до 21:00 необходимо отправить " +
                            "отчёт(ы) по питомцу(ам). Спасибо!");
        });
    }

//    @Scheduled(cron = "0 00 10 * * *")
    public void sendReminderIfNoReports() {
        // 1) Если два дня нет отчётов, то напоминание от бота
        //  и/или
        // 2) если у Adopter есть ProbationPeriod-ы, у которых поле
        // boolean needToSendVolunteersComment равен true, то надо отправить
        // сообщение волонтёра (String volunteersComment)
        // ??можно соединить, можно разделить на 2 метода??
        //
        //Так как по ТЗ замечание для усыновителя пишет волонтёр, то
        // содержимое сообщения можно вручную вбить в бд чисто для теста метода
    }

    //    @Scheduled(cron = "0 00 10 * * *")
    public void sendCongratulations() {
        // Если у Adopter есть успешно законченный ProbationPeriod, то надо
        // отправить поздравление
    }

    //    @Scheduled(cron = "0 00 10 * * *")
    public void sendRefusal() {
        // Если Adopter не прошёл ProbationPeriod, то надо
        // отправить уведомление и инфу, что делать дальше
    }

    //    @Scheduled(cron = "0 00 10 * * *")
    public void sendProlongationNotification() {
        // Если у Adopter продлён ProbationPeriod, то надо
        // отправить уведомление об этом
        // ?? может быть, добавить ещё поле boolean в ProbationPeriod ??
    }
}
