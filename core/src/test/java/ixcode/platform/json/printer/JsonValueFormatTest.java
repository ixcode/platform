package ixcode.platform.json.printer;

import ixcode.platform.json.printer.JsonValueFormat;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;


public class JsonValueFormatTest {

    private final JsonValueFormat jsonValueFormat = new JsonValueFormat();

    @Test
    public void string_value() {
        String result = jsonValueFormat.format("hello");

        assertThat(result).isEqualTo("\"hello\"");
    }

    @Test
    public void integer_value() {
        String result = jsonValueFormat.format(3456);

        assertThat(result).isEqualTo("3456");
    }

    @Test
    public void double_value() {
        String result = jsonValueFormat.format(3424234.54645645);

        assertThat(result).isEqualTo("3424234.54645645");
    }

    @Test
    public void long_value() {
        String result = jsonValueFormat.format(3424234374628374628L);

        assertThat(result).isEqualTo("3424234374628374628");
    }

    @Test
    public void null_value() {
        String result = jsonValueFormat.format(null);

        assertThat(result).isEqualTo("null");
    }

    @Test
    public void true_value() {
        String result = jsonValueFormat.format(true);

        assertThat(result).isEqualTo("true");
    }

    @Test
    public void false_value() {
        String result = jsonValueFormat.format(false);

        assertThat(result).isEqualTo("false");
    }

}