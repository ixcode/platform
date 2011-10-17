package ixcode.platform.text.format;

public class StringFormat extends AbstractFormat<String> {

    public String parseString(String value) {
        return value;
    }

    public String format(String source) {
        return source;
    }
}