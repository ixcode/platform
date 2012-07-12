package ixcode.platform.cryptography;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import static javax.crypto.Cipher.ENCRYPT_MODE;

public class AesEncryption implements Encryption {
    private final KeyGenerator keyGenerator;
    private final Cipher cipher;
    private final Key key;

    public AesEncryption() {
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
            cipher = Cipher.getInstance("AES");
            key = keyGenerator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override public String encrypt(String source) {
        try {
            cipher.init(ENCRYPT_MODE, key);
            return new String(cipher.doFinal(source.getBytes("UTF8")), "UTF8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override public String decrypt(String string) {
        return null;
    }
}