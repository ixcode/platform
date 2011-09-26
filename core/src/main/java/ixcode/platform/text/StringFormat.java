package ixcode.platform.text;

public class StringFormat implements Format<String> {

    public String parseString(String value) {
        return value;
    }

    public String format(String source) {
        return source;
    }
}