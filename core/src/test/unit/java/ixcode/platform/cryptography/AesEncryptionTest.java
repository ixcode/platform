package ixcode.platform.cryptography;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class AesEncryptionTest {

    @Test
    public void should_make_the_same_encryption() {

        AesEncryption aesEncryption = new AesEncryption();

        String encrypted_a = aesEncryption.encrypt("Some secret message");
        String encrypted_b = aesEncryption.encrypt("Some secret message");
        String encrypted_c = aesEncryption.encrypt("Some other message");

        assertThat(encrypted_a, is(not("Some secret message")));
        assertThat(encrypted_a, is(encrypted_b));
        assertThat(encrypted_c, is(not(encrypted_a)));

        aesEncryption = new AesEncryption();

        String encrypted_d = aesEncryption.encrypt("Some secret message");
        assertThat(encrypted_d, is(not(encrypted_b)));


    }


}