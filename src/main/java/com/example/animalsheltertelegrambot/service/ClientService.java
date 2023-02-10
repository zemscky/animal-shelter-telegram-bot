package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.Client;
import com.example.animalsheltertelegrambot.repositories.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void saveClient(Long chatId) {
        Client client = new Client();
        client.setChatId(chatId);
        clientRepository.save(client);
    }

    public boolean clientExists(Long chatId) {
        return clientRepository.existsById(chatId);
    }
}
