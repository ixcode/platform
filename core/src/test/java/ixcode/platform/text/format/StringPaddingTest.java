package ixcode.platform.text.format;

import org.junit.*;

import static ixcode.platform.text.format.StringPadding.padRight;
import static org.fest.assertions.Assertions.assertThat;

public class StringPaddingTest {

    @Test
    public void can_pad_a_string() {
        String result = padRight("Hello", 10);

        assertThat(result).isEqualTo("Hello     ");
    }

    @Test
    public void trims_longer_strings() {
        String result = padRight("This is quite long", 4);

        assertThat(result).isEqualTo("This");
    }
}