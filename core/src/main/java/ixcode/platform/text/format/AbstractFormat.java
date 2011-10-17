package ixcode.platform.text.format;

public abstract class AbstractFormat<T> implements Format<T> {
    @Override public abstract T parseString(String source);

    @Override public abstract String format(T source);

    @Override
    public String formatObject(Object object) {
        return format((T)object);
    }
}