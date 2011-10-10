package ixcode.platform.json.printer;

import ixcode.platform.json.printer.JsonValueFormat;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JsonValueFormatTest {

    private final JsonValueFormat jsonValueFormat = new JsonValueFormat();

    @Test
    public void string_value() {
        String result = jsonValueFormat.format("hello");

        assertThat(result, is("\"hello\""));
    }

    @Test
    public void integer_value() {
        String result = jsonValueFormat.format(3456);

        assertThat(result, is("3456"));
    }

    @Test
    public void double_value() {
        String result = jsonValueFormat.format(3424234.54645645);

        assertThat(result, is("3424234.54645645"));
    }

    @Test
    public void long_value() {
        String result = jsonValueFormat.format(3424234374628374628L);

        assertThat(result, is("3424234374628374628"));
    }

    @Test
    public void null_value() {
        String result = jsonValueFormat.format(null);

        assertThat(result, is("null"));
    }

    @Test
    public void true_value() {
        String result = jsonValueFormat.format(true);

        assertThat(result, is("true"));
    }

    @Test
    public void false_value() {
        String result = jsonValueFormat.format(false);

        assertThat(result, is("false"));
    }

}