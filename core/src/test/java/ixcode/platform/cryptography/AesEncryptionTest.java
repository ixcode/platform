package ixcode.platform.cryptography;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;


public class AesEncryptionTest {

    @Test
    public void should_make_the_same_encryption() {

        AesEncryption aesEncryption = new AesEncryption();

        String encrypted_a = aesEncryption.encrypt("Some secret message");
        String encrypted_b = aesEncryption.encrypt("Some secret message");
        String encrypted_c = aesEncryption.encrypt("Some other message");

        assertThat(encrypted_a).isNotEqualTo("Some secret message");
        assertThat(encrypted_a).isEqualTo(encrypted_b);
        assertThat(encrypted_c).isNotEqualTo(encrypted_a);

        aesEncryption = new AesEncryption();

        String encrypted_d = aesEncryption.encrypt("Some secret message");
        assertThat(encrypted_d).isNotEqualTo(encrypted_b);


    }


}