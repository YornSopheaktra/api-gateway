package com.spt.utils;


import com.spt.config.AppProperties;
import com.spt.exption.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class APIManagerUtils {

    private final AppProperties appProperties;

    public String genApiKey() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    public String genClientId() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .toUpperCase();
    }

    public String getAPIKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public String genSecret() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public String encryptedSecret(String clientId,String clientSecret) {
        try {
            SecretKey secretKey = SecurityUtils
                    .generateEncryptionString(clientSecret
                            , appProperties.getSecurity().getSalt());
            return SecurityUtils.convertSecretKeyToString(secretKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        throw new AuthException();
    }

    public String compareEncryption(String clientId,String inputString) {
        try {
            SecretKey secretKey = SecurityUtils.generateEncryptionString(inputString
                    , appProperties.getSecurity().getSalt());
            return SecurityUtils.convertSecretKeyToString(secretKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        throw new AuthException();
    }


    public String encryptedSecretV2(String clientId, String clientSecret) {
        try {
            return SecurityUtils.encrypt(clientId, appProperties.getSecurity().getSalt(), clientSecret);
        } catch (Exception e) {
        }
        throw new AuthException();
    }
    public String compareEncryptionV2(
            String clientId, String secret) {
        return this.encryptedSecretV2(clientId, secret);
    }

}
