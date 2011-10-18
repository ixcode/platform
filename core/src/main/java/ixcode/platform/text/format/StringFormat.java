package ixcode.platform.text.format;

public class StringFormat extends AbstractFormat {

    public String parseString(String value) {
        return value;
    }

    public String format(Object source) {
        return (String)source;
    }
}