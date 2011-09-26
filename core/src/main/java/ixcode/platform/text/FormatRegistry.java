package ixcode.platform.text;

import java.net.*;
import java.util.*;

public class FormatRegistry {

    private final Map<Class<?>, Format<?>> formatMap = new HashMap<Class<?>, Format<?>>();

    public FormatRegistry() {
        loadStandardFormats();
    }

    private void loadStandardFormats() {
        addFormat(Object.class, new ObjectFormat());
        addFormat(String.class, new StringFormat());
        addFormat(Integer.class, new IntegerFormat());
        addFormat(int.class, new IntegerFormat());
        addFormat(Date.class, new UtcDateFormat());
        addFormat(URI.class, new UriFormat());
    }

    public <T> void  addFormat(Class<T> targetClass, Format<T> format) {
        formatMap.put(targetClass, format);
    }

    public <T> Format<T> findFormatFor(Class<T> targetClass) {
        if (!formatMap.containsKey(targetClass)) {
            throw new RuntimeException("Could not find targetClass " + targetClass.getName() + " in format registry");
        }
        return (Format<T>)formatMap.get(targetClass);
    }
}