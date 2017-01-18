package com.lotrading.controlwhapp.utilities;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;


/**
 * Created by DESARROLLO PORTATIL on 13/12/2016.
 */
public class Encryption {
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    public static final String DES_ENCRYPTION_SCHEME = "DES";
    public static final String DEFAULT_ENCRYPTION_KEY = "9asd64jd3sdg88dhg97"
            + "ipuobf6546938q4564asdf654asdf6464asd6f54q9a63z85fv741nmy54k6jh4ksdfklj"
            + "654$/=plsdfjbasdiry346978asfdjbh23q0984xn00874cn20939201cm'12804";

    private KeySpec keySpec;
    private SecretKeyFactory keyFactory;
    private Cipher cipher;

    private static final String UNICODE_FORMAT = "UTF8";

    public Encryption() throws EncryptionException {
        this("DESede");
    }

    public Encryption(String encryptionScheme) throws EncryptionException {
        this(encryptionScheme, DEFAULT_ENCRYPTION_KEY);
    }

    /**
     * <p>
     * Crea una instancia de la clase utilitaria.
     * </p>
     *
     * @param encryptionScheme
     *            el tipo de scheme de encripcion a usar.
     * @param encryptionKey
     *            llave de encripcion a utilizar
     * @throws EncryptionException
     */
    public Encryption(String encryptionScheme, String encryptionKey)
            throws EncryptionException {

        if (encryptionKey == null)
            throw new IllegalArgumentException("The encryption key is null");
        if (encryptionKey.trim().length() < 24)
            throw new IllegalArgumentException(
                    "The encryption key is less than 24 characters");
        try {
            byte[] keyAsBytes = encryptionKey.getBytes(UNICODE_FORMAT);

            if (encryptionScheme.equals(DESEDE_ENCRYPTION_SCHEME)) {
                keySpec = new DESedeKeySpec(keyAsBytes);
            } else if (encryptionScheme.equals(DES_ENCRYPTION_SCHEME)) {
                keySpec = new DESKeySpec(keyAsBytes);
            } else {
                throw new IllegalArgumentException(
                        "Encryption schema not supported: "
                                + encryptionScheme);
            }

            keyFactory = SecretKeyFactory.getInstance(encryptionScheme);
            cipher = Cipher.getInstance(encryptionScheme);

        } catch (InvalidKeyException e) {
            throw new EncryptionException(e);
        } catch (UnsupportedEncodingException e) {
            throw new EncryptionException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException(e);
        } catch (NoSuchPaddingException e) {
            throw new EncryptionException(e);
        }

    }

    public String encrypt(String unencryptedString) throws EncryptionException {
        if (unencryptedString == null || unencryptedString.trim().length() == 0)
            throw new IllegalArgumentException(
                    "Encrypted chain is null or empty");

        try {
            SecretKey key = keyFactory.generateSecret(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] ciphertext = cipher.doFinal(cleartext);

            BASE64Encoder base64encoder = new BASE64Encoder();
            return base64encoder.encode(ciphertext);
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    public String decrypt(String encryptedString) throws EncryptionException {
        if (encryptedString == null || encryptedString.trim().length() <= 0)
            throw new IllegalArgumentException(
                    "Encrypted chain is null or empty");

        try {
            SecretKey key = keyFactory.generateSecret(keySpec);
            cipher.init(Cipher.DECRYPT_MODE, key);
            BASE64Decoder base64decoder = new BASE64Decoder();
            byte[] cleartext = base64decoder.decodeBuffer(encryptedString);
            byte[] ciphertext = cipher.doFinal(cleartext);

            return bytes2String(ciphertext);
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    private static String bytes2String(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            stringBuffer.append((char) bytes[i]);
        }
        return stringBuffer.toString();
    }

    public static class EncryptionException extends Exception {
        /**
         *
         */
        private static final long serialVersionUID = -370818602229490820L;

        public EncryptionException(Throwable t) {
            super(t);
        }

        public EncryptionException(String s) {
            super(s);
        }

    }

}