package ixcode.platform.exception;

public class NullValueException extends RuntimeException {

    public NullValueException(String message) {
        super(message);
    }

    public static <T> T notNull(T result, String message) {
        if (result == null) {
            throw new NullValueException(message);
        }
        return result;
    }
}