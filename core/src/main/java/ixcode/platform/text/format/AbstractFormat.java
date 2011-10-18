package ixcode.platform.text.format;

public abstract class AbstractFormat implements Format {
    @Override public abstract <T> T parseString(String source);

    @Override public abstract String format(Object source);

}