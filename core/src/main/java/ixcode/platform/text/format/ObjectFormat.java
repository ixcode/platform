package ixcode.platform.text.format;

public class ObjectFormat extends AbstractFormat {
    public Object parseString(String value) {
        throw new IllegalStateException("Cannot parse a random string to an object! not enough info " + value);
    }

    public String format(Object source) {
        if (source == null) {
            return "";
        }
        return source.toString();
    }
}