package com.stych.android.util;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtl {
    private static final String KEY_STRING = "3rXuNkjQ2mL4TZJJ";
    private static final String IV_STRING = "7cNRBV";

    public static byte[] encrypt(String ivStr, String keyStr, byte[] bytes) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(ivStr.getBytes());
        byte[] ivBytes = md.digest();

        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        sha.update(keyStr.getBytes());
        byte[] keyBytes = sha.digest();

        return encrypt(ivBytes, keyBytes, bytes);
    }

    static byte[] encrypt(byte[] ivBytes, byte[] keyBytes, byte[] bytes) throws Exception {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(bytes);
    }

    public static byte[] decrypt(String ivStr, String keyStr, byte[] bytes) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(ivStr.getBytes());
        byte[] ivBytes = md.digest();

        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        sha.update(keyStr.getBytes());
        byte[] keyBytes = sha.digest();

        return decrypt(ivBytes, keyBytes, bytes);
    }

    static byte[] decrypt(byte[] ivBytes, byte[] keyBytes, byte[] bytes) throws Exception {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(bytes);
    }

    public static String encryptStrAndToBase64(String ivStr, String keyStr, String enStr) throws Exception {
        byte[] bytes = encrypt(keyStr, keyStr, enStr.getBytes("UTF-8"));
        return new String(Base64.encode(bytes, Base64.DEFAULT), "UTF-8");
    }

    public static String decryptStrAndFromBase64(String ivStr, String keyStr, String deStr) throws Exception {
        byte[] bytes = decrypt(keyStr, keyStr, Base64.decode(deStr.getBytes("UTF-8"), Base64.DEFAULT));
        return new String(bytes, "UTF-8");
    }

    public static String encrypt(String plainText) {
        try {
            byte[] bytes = EncryptionUtl.encrypt(IV_STRING, KEY_STRING, plainText.getBytes());
            return new String(bytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plainText;
    }

    public static String decrypt(String cipherText) {
        try {
            byte[] deans = EncryptionUtl.decrypt(IV_STRING, KEY_STRING, cipherText.getBytes());
            return new String(deans, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }
}
