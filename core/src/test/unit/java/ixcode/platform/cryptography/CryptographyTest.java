package ixcode.platform.cryptography;

import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;

public class CryptographyTest {

    @Test
    public void can_encrypt_a_value_one_way() throws Exception {

        String mySecret = "shhh, this is me secret treshure";

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        Key key = keyGenerator.generateKey();

        Cipher aes = Cipher.getInstance("AES");
        aes.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedBytes = aes.doFinal(mySecret.getBytes("UTF8"));

        String encrypted = new String(encryptedBytes, "UTF8");





    }


}