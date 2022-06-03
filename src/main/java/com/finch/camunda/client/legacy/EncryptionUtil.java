package com.finch.camunda.client.legacy;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptionUtil implements Serializable {

    private static SecretKeySpec secretKey;
    private static String decryptedString;
    private static String encryptedString;

    private static final Logger logger = LoggerFactory.getLogger(EncryptionUtil.class);

    public static String encrypt(String strToEncrypt) {

        if (StringUtils.isBlank(strToEncrypt)) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
            setEncryptedString(Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8))));
            return getEncryptedString();
        } catch (Exception e) {
            logger.error(Util.retornaMensagem("criptografia.errorNaEncriptografiaDeString"));
        }
        return null;
    }

    public static String decrypt(String strToDecrypt) {

        if (StringUtils.isBlank(strToDecrypt)) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
            setDecryptedString(new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt))));
            return getDecryptedString();
        } catch (Exception e) {
            logger.error(Util.retornaMensagem("criptografia.errorNaDecriptografiaDeString"));
        }
        return null;
    }

    public static String decryptForLogin(final String key, final String strToDecrypt) {

        if (StringUtils.isBlank(strToDecrypt)) {
            return null;
        }

        try {
            final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            final SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            final IvParameterSpec ivspec = new IvParameterSpec(Arrays.copyOf(key.getBytes(), 16));
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            final byte[] original = cipher.doFinal(Base64.decodeBase64(strToDecrypt));
            final String originalString = new String(original);
            return originalString.trim();
        } catch (Exception e) {
            logger.error(Util.retornaMensagem("criptografia.errorNaDecriptografiaDeString.login"));
        }
        return "";
    }

    public static void setKey() {
        MessageDigest sha;
        byte[] key;
        try {
            final String myKey = "wBrSY&K8#L2z3p#Q2k8f";
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            logger.error(Util.retornaMensagem("criptografia.errorAlgoritimoNaoSuportado"));
        }
    }

    public static SecretKeySpec getSecretKey() {
        return secretKey;
    }

    public static void setSecretKey(SecretKeySpec secretKey) {
        EncryptionUtil.secretKey = secretKey;
    }


    private static String getDecryptedString() {
        return decryptedString;
    }

    private static void setDecryptedString(String decryptedString) {
        EncryptionUtil.decryptedString = decryptedString;
    }

    private static String getEncryptedString() {
        return encryptedString;
    }

    private static void setEncryptedString(String encryptedString) {
        EncryptionUtil.encryptedString = encryptedString;
    }
}
