package com.ajpasigado.qpicalculator;

import android.util.Base64;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESCrypt {
    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    public static String encrypt(String value) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(1, key);
        return Base64.encodeToString(cipher.doFinal(value.getBytes("utf-8")), 1);
    }

    public static String decrypt(String value) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(2, key);
        return new String(cipher.doFinal(Base64.decode(value, 1)), "utf-8");
    }

    private static Key generateKey() throws Exception {
        return new SecretKeySpec(KEY.getBytes(), ALGORITHM);
    }
}
