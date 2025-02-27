package com.spt.utils;


import java.util.UUID;

public class APIManagerUtils {

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

/*    public String encryptedSecret(String clientId,String clientSecret) {
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
        throw new AppException();
    }*/
/*
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
        throw new AppException();
    }


    public String encryptedSecretV2(String clientId, String clientSecret) {
        try {
            return SecurityUtils.encrypt(clientId, appProperties.getSecurity().getSalt(), clientSecret);
        } catch (Exception e) {
        }
        throw new AppException();
    }
    public String compareEncryptionV2(
            String clientId, String secret) {
        return this.encryptedSecretV2(clientId, secret);
    }*/

}
