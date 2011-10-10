package ixcode.platform.text;

public interface  Format<T> {
    T parseString(String source);

    String format(T source);
    String formatObject(Object source);

}