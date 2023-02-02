package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.Contact;
import com.example.animalsheltertelegrambot.models.InfoMessage;
import com.example.animalsheltertelegrambot.repositories.AnimalRepository;
import com.example.animalsheltertelegrambot.repositories.ClientRepository;
import com.example.animalsheltertelegrambot.repositories.ContactRepository;
import com.example.animalsheltertelegrambot.repositories.MessageRepository;
import com.pengrad.telegrambot.TelegramBot;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    ClientRepository clientRepository = mock(ClientRepository.class);
    AnimalRepository animalRepository = mock(AnimalRepository.class);
    MessageRepository messageRepository = mock(MessageRepository.class);
    ContactRepository contactRepository = mock(ContactRepository.class);
    TelegramBot telegramBot = mock(TelegramBot.class);
    SendResponse sendResponse = mock(SendResponse.class);
    SendResponse sendResponse2 = mock(SendResponse.class);

    ClientService clientService = new ClientService(clientRepository, animalRepository, messageRepository, contactRepository);

    @BeforeEach
    void setUp() {
        clientService.setTelegramBot(telegramBot);
    }

    @Test
    void sendMessageTest() {
        when(telegramBot.execute(any())).thenReturn(sendResponse);
        SendResponse response = this.clientService.sendMessage(123L, "test", "testMessage");
        Assertions.assertThat(response).isEqualTo(sendResponse);
        Assertions.assertThat(response).isNotEqualTo(sendResponse2);
        Assertions.assertThat(response.isOk()).isFalse();

        when(sendResponse.isOk()).thenReturn(true);
        response = this.clientService.sendMessage(123L, "test", "testMessage");
        Assertions.assertThat(response.isOk()).isTrue();
    }

    @Test
    void sendCallbackMessageTest() {
        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(this.contactRepository.findById(123L)).thenReturn(Optional.empty());
        Assertions.assertThat(clientService.sendCallbackMessage(123L)).isEqualTo(sendResponse);

        when(this.contactRepository.findById(123L)).thenReturn(Optional.of(new Contact(123L, "+79119009090")));
        Assertions.assertThat(clientService.sendCallbackMessage(123L)).isEqualTo(sendResponse);
    }

    @Test
    void sendContactSavedMessageTest() {
        when(telegramBot.execute(any())).thenReturn(sendResponse);

        SendResponse response = this.clientService.sendContactSavedMessage(123L);
        Assertions.assertThat(response).isEqualTo(sendResponse);
        Assertions.assertThat(response.isOk()).isFalse();

        when(sendResponse.isOk()).thenReturn(true);
        response = this.clientService.sendContactSavedMessage(123L);

        Assertions.assertThat(response.isOk()).isTrue();
    }

    @Test
    void sendInfoMessageTest() {
        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(this.messageRepository.findById("/test")).thenReturn(Optional.of(new InfoMessage("/test", "Test message")));

        SendResponse response = this.clientService.sendInfoMessage(123L, "/test");
        Assertions.assertThat(response).isEqualTo(sendResponse);
        Assertions.assertThat(response.isOk()).isFalse();

        when(this.contactRepository.findById(123L)).thenReturn(Optional.empty());
        when(sendResponse.isOk()).thenReturn(true);

        response = this.clientService.sendInfoMessage(123L, "/test");
        Assertions.assertThat(response).isEqualTo(sendResponse);
        Assertions.assertThat(response.isOk()).isTrue();
    }

    @Test
    void isCommandTest() {
        boolean isCommand1 = this.clientService.isCommand("/test");
        boolean isCommand2 = this.clientService.isCommand("test");

        Assertions.assertThat(isCommand1).isTrue();
        Assertions.assertThat(isCommand2).isFalse();
    }

    @Test
    void isInfoRequestTest() {
        when(this.messageRepository.findById("/testFalse")).thenReturn(Optional.empty());
        when(this.messageRepository.findById("/testTrue")).thenReturn(Optional.of(new InfoMessage("test", "test")));

        boolean isInfoRequestFalse = this.clientService.isInfoRequest("/testFalse");
        boolean isInfoRequestTrue = this.clientService.isInfoRequest("/testTrue");

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
        boolean isValid = this.clientService.isMobileNumberValid(number);
        boolean isValid2 = this.clientService.isMobileNumberValid(any);

        Assertions.assertThat(isValid).isTrue();
        Assertions.assertThat(isValid2).isFalse();
    }

    @Test
    void getNotFoundInfoMessageTest() {
        InfoMessage infoMessage = this.clientService.getNotFoundInfoMessage();
        Assertions.assertThat(infoMessage.getTag()).isEqualTo("not found");
    }

    @Test
    void saveContactTest() {
        Contact returnedContact = new Contact(123L, "test");
        when(this.contactRepository.save(any())).thenReturn(returnedContact);

        Assertions.assertThat(this.clientService.saveContact(123L, "+79595553535")).isEqualTo(returnedContact);
    }
}