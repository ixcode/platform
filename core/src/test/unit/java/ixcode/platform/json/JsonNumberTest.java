package ixcode.platform.json;

import org.junit.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JsonNumberTest {

    @Test
    public void stores_an_integer() {
        int value = Integer.MAX_VALUE;

        JsonNumber number = new JsonNumber(value);

        assertThat(number.intValue(), is(value));
    }

    @Test
    public void stores_a_double() {
        double value = Double.MAX_VALUE;

        JsonNumber number = new JsonNumber(value);

        assertThat(number.doubleValue(), is(value));
    }


}