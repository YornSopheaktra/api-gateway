package com.spt.service.imp;

import com.spt.config.ClientConfig;
import com.spt.exption.ServerException;
import com.spt.model.entity.Client;
import com.spt.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImp implements ClientService {

    private final ClientConfig client;

    @Override
    public Client getclient(String clientId) {
        if (!client.getConfig().containsKey(clientId)){
            throw new ServerException("M01", "Invalid client credentials.");
        }
        return client.getConfig().get(clientId);
    }
}
