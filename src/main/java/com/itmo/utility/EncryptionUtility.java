package com.itmo.utility;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;

/**
 * Created by anastasia.sulyagina
 */
public class EncryptionUtility {

    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    public static String encrypt(String value)
    {
        try {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(EncryptionUtility.ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
            return new BASE64Encoder().encode(encryptedByteValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String decrypt(String value)
    {
        try{
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(EncryptionUtility.ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte [] decryptedValue64 = new BASE64Decoder().decodeBuffer(value);
            byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
            return new String(decryptedByteValue,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static Key generateKey()
    {
        Key key = new SecretKeySpec(EncryptionUtility.KEY.getBytes(), EncryptionUtility.ALGORITHM);
        return key;
    }
}
