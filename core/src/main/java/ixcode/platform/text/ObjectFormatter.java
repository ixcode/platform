package ixcode.platform.text;

public class ObjectFormatter {

    private FormatRegistry formatRegistry = new FormatRegistry();

    public <T> String format(T source) {
        if (source == null) {
            return null;
        }
        Class<T> type = (Class<T>) source.getClass();
        Format<T> format = formatRegistry.findFormatFor(type);
        return format.format(source);
    }
}