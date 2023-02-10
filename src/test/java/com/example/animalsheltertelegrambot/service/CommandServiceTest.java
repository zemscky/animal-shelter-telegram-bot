package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.Client;
import com.example.animalsheltertelegrambot.models.InfoMessage;
import com.example.animalsheltertelegrambot.repositories.AnimalRepository;
import com.example.animalsheltertelegrambot.repositories.AdopterRepository;
import com.example.animalsheltertelegrambot.repositories.ContactRepository;
import com.example.animalsheltertelegrambot.repositories.InfoMessageRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandServiceTest {
    AdopterRepository adopterRepository = mock(AdopterRepository.class);
    AnimalRepository animalRepository = mock(AnimalRepository.class);
    InfoMessageRepository messageRepository = mock(InfoMessageRepository.class);
    ContactRepository contactRepository = mock(ContactRepository.class);
    TelegramBot telegramBot = mock(TelegramBot.class);
    SendResponse sendResponse = mock(SendResponse.class);
    InlineKeyboardMarkup keyboardMarkup = mock(InlineKeyboardMarkup.class);
    BaseResponse baseResponse = mock(BaseResponse.class);

    CommandService commandService = new CommandService(adopterRepository, animalRepository, messageRepository, contactRepository);

    @BeforeEach
    void setUp() {
        this.commandService.setTelegramBot(telegramBot);
    }

    @Test
    void sendMessageTest() {
        when(telegramBot.execute(any())).thenReturn(sendResponse);
        SendResponse response = this.commandService.sendMessage(123L, "test", "testMessage",keyboardMarkup);
        Assertions.assertThat(response).isEqualTo(sendResponse);
        Assertions.assertThat(response.isOk()).isFalse();

        when(sendResponse.isOk()).thenReturn(true);
        response = this.commandService.sendMessage(123L, "test", "testMessage", keyboardMarkup);
        Assertions.assertThat(response.isOk()).isTrue();
    }

    @Test
    void sendCallbackMessageTest() {
        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(this.contactRepository.findById(123L)).thenReturn(Optional.empty());
        Assertions.assertThat(commandService.sendCallbackMessage(123L, keyboardMarkup)).isEqualTo(sendResponse);

        when(this.contactRepository.findById(123L)).thenReturn(Optional.of(new Client(123L, "+79119009090")));
        Assertions.assertThat(commandService.sendCallbackMessage(123L, keyboardMarkup)).isEqualTo(sendResponse);
    }

    @Test
    void sendContactSavedMessageTest() {
        when(telegramBot.execute(any())).thenReturn(sendResponse);

        SendResponse response = this.commandService.sendContactSavedMessage(123L);
        Assertions.assertThat(response).isEqualTo(sendResponse);
        Assertions.assertThat(response.isOk()).isFalse();

        when(sendResponse.isOk()).thenReturn(true);
        response = this.commandService.sendContactSavedMessage(123L);

        Assertions.assertThat(response.isOk()).isTrue();
    }

    @Test
    void sendInfoMessageTest() {
        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(this.messageRepository.findById("/test")).thenReturn(Optional.of(new InfoMessage("/test", "Test message")));

        SendResponse response = this.commandService.sendInfoMessage(123L, "/test", keyboardMarkup);
        Assertions.assertThat(response).isEqualTo(sendResponse);
        Assertions.assertThat(response.isOk()).isFalse();

        when(this.contactRepository.findById(123L)).thenReturn(Optional.empty());
        when(sendResponse.isOk()).thenReturn(true);

        response = this.commandService.sendInfoMessage(123L, "/test", keyboardMarkup);
        Assertions.assertThat(response).isEqualTo(sendResponse);
        Assertions.assertThat(response.isOk()).isTrue();
    }

    @Test
    void isCommandTest() {
        boolean isCommand1 = this.commandService.isCommand("/test");
        boolean isCommand2 = this.commandService.isCommand("test");

        Assertions.assertThat(isCommand1).isTrue();
        Assertions.assertThat(isCommand2).isFalse();
    }

    @Test
    void isInfoRequestTest() {
        when(this.messageRepository.findById("/testFalse")).thenReturn(Optional.empty());
        when(this.messageRepository.findById("/testTrue")).thenReturn(Optional.of(new InfoMessage("test", "test")));

        boolean isInfoRequestFalse = this.commandService.isInfoRequest("/testFalse");
        boolean isInfoRequestTrue = this.commandService.isInfoRequest("/testTrue");

        Assertions.assertThat(isInfoRequestFalse).isFalse();
        Assertions.assertThat(isInfoRequestTrue).isTrue();
    }

    public static List<Arguments> isMobileNumberValidTestSuites() {
        return List.of(
                Arguments.of("+79119902516", "gsd454"),
                Arguments.of("79119902516", "+7030 498 53 90"),
                Arguments.of("89119902516", "8911990-25-16")
        );
    }

    @ParameterizedTest
    @MethodSource("isMobileNumberValidTestSuites")
    void isMobileNumberValidTest(String number, String any) {
        boolean isValid = this.commandService.isMobileNumberValid(number);
        boolean isValid2 = this.commandService.isMobileNumberValid(any);

        Assertions.assertThat(isValid).isTrue();
        Assertions.assertThat(isValid2).isFalse();
    }

    @Test
    void getNotFoundInfoMessageTest() {
        InfoMessage infoMessage = this.commandService.getNotFoundInfoMessage();
        Assertions.assertThat(infoMessage.getTag()).isEqualTo("not found");
    }

    @Test
    void saveContactTest() {
        Client returnedClient = new Client(123L, "test");
        when(this.contactRepository.save(any())).thenReturn(returnedClient);

        Assertions.assertThat(this.commandService.saveContact(123L, "+79595553535")).isEqualTo(returnedClient);
    }

    @Test
    void sendCallbackQueryResponseTest() {
        when(telegramBot.execute(any())).thenReturn(baseResponse);
        Assertions.assertThat(this.commandService.sendCallbackQueryResponse("123")).isEqualTo(baseResponse);
    }
}