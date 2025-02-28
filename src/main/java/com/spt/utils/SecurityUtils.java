package com.spt.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
public class SecurityUtils {

    private static Logger LOGGER =
            LoggerFactory.getLogger(SecurityUtils.class);

    public static RSAPublicKey readPublicKey(String content) {
        try {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Loading public key.");
            String publicKeyPEM = content
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replaceAll(System.lineSeparator(), "")
                    .replace("-----END PUBLIC KEY-----", "");
            byte[] encoded = org.apache.commons.codec.binary.Base64.decodeBase64(publicKeyPEM);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception exception) {
            LOGGER.error("Loading private key error.");
        }
        return null;
    }

    public static RSAPrivateKey readPrivateKey(String content) {
        try {
            String privateKeyPEM = content
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll(System.lineSeparator(), "")
                    .replace("-----END PRIVATE KEY-----", "");
            byte[] encoded = org.apache.commons.codec.binary.Base64.decodeBase64(privateKeyPEM);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception exception) {
            LOGGER.error("Loading private key error.");
        }
        return null;
    }

    public static SecretKey generateEncryptionString(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey originalKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return originalKey;
    }

    /* Converting Secret key into String */
    public static String convertSecretKeyToString(SecretKey secretKey) throws NoSuchAlgorithmException {
        // Converting the Secret Key into byte array
        byte[] rawData = secretKey.getEncoded();
        // Getting String - Base64 encoded version of the Secret Key
        String encodedKey = Base64.getEncoder().encodeToString(rawData);
        return encodedKey;
    }

    /* Converting String into Secret key into */
    public static SecretKey convertStringToSecretKeyto(String encodedKey) {
        // Decoding the Base64 encoded string into byte array
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        // Rebuilding the Secret Key using SecretKeySpec Class
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        return originalKey;
    }

    public static String sign(String plainText,
                              PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes(StandardCharsets.UTF_8));
        byte[] signature = privateSignature.sign();
        return Base64.getEncoder()
                .encodeToString(signature);
    }

    public static boolean verify(String plainText, String signature, PublicKey publicKey) {
        try {
            Signature publicSignature = Signature.getInstance("SHA256withRSA");
            publicSignature.initVerify(publicKey);
            publicSignature.update(plainText.getBytes(StandardCharsets.UTF_8));
            byte[] signatureBytes = Base64.getDecoder()
                    .decode(signature);
            return publicSignature.verify(signatureBytes);
        } catch (Exception exception) {
            log.error("Error verify signature.", exception);
            return false;
        }
    }
    
    public static String encrypt(String key, String SALT, String strToEncrypt) {
        try {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(key.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.error("Error on encryption.",e);
        }
        return null;
    }

    public static String decrypt(String key, String salt, String strToDecrypt) {
        try {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            log.error("Error on decrypt.",e);
        }
        return null;
    }


}
