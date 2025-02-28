package com.spt.service;


import com.spt.exption.AuthException;
import com.spt.exption.code.AuthCode;
import com.spt.model.entity.Client;
import com.spt.utils.APIManagerUtils;
import com.spt.utils.HttpHeaderUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class AuthenticateManager {

    private final ClientService clientService;
    private final APIManagerUtils apiManagerUtils;

    private String apiKey;
    private String clientId;
    private String clientSecret;

    private HttpHeaders httpHeaders;

    public AuthenticateManager instant(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
        return this;
    }


    public AuthenticateManager instant(String clientId) {
        this.setClientId(clientId);
        return this;
    }

    public AuthenticateManager isValidClient() {
        this.setClientId(HttpHeaderUtils.clientId(httpHeaders));
        this.setClientSecret(HttpHeaderUtils.clientSecret(httpHeaders));
        this.setApiKey(HttpHeaderUtils.apiKey(httpHeaders));
        clientService.getclient(this.getClientId());
        return this;
    }


    public Client getClient() {
        this.setClientId(HttpHeaderUtils.clientId(httpHeaders));
        return clientService.getclient(this.getClientId());
    }


    public void validateClientSecret(Client client, String secret) {
        String secretString = client.getSecret();
        if (!secretString
                .equals(apiManagerUtils.compareEncryption(client.getId(), secret))) {
            throw new AuthException(AuthCode.AUTH_INVALID_CLIENT_CREDENTIALS);
        }
    }

    public void validateApiKey(Client client, String apiKey) {
        if (!client.getApiKey().equals(apiKey))
            throw new AuthException(AuthCode.AUTH_APIKEY_INVALID);
    }

    public void validateClientSecret(Client client, String deviceId, String secret) {
        String secretString = client.getSecret();
        if (!secretString.equals(secret)) {
            throw new AuthException(AuthCode.AUTH_INVALID_CLIENT_CREDENTIALS);
        }
    }


}
