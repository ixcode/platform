package ixcode.platform.text;

import org.junit.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ixcode.platform.text.StringPadding.padRight;

public class StringPaddingTest {

    @Test
    public void can_pad_a_string() {
        String result = padRight("Hello", 10);

        assertThat(result, is("Hello     "));
    }

    @Test
    public void trims_longer_strings() {
        String result = padRight("This is quite long", 4);

        assertThat(result, is("This"));
    }
}