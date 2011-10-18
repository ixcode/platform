package ixcode.platform.text.format;

public interface  Format {
    <T> T parseString(String source);

    String format(Object source);
}