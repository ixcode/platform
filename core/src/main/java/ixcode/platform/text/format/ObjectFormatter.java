package ixcode.platform.text.format;

public class ObjectFormatter {

    private FormatRegistry formatRegistry = new FormatRegistry();

    public <T> String format(T source) {
        if (source == null) {
            return null;
        }
        Class<T> type = (Class<T>) source.getClass();
        return formatRegistry.findFormatFor(type).format(source);
    }
}