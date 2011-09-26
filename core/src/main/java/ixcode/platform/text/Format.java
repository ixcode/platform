package ixcode.platform.text;

public interface  Format<T> {
    T parseString(String value);

    String format(T source);
}