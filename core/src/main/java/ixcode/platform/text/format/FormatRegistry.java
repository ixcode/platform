package ixcode.platform.text.format;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FormatRegistry {

    private static final Map<Class<?>, Format> USER_REGISTERED_FORMATS = new HashMap<Class<?>, Format>();

    private final Map<Class<?>, Format> formatMap = new HashMap<Class<?>, Format>();

    public static void registerFormat(Class<?> sourceClass, Format format) {
        USER_REGISTERED_FORMATS.put(sourceClass, format);
    }

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
        addFormat(UUID.class, new UuidFormat());
        addUserRegisteredFormats();
    }

    private void addUserRegisteredFormats() {
        for (Map.Entry<Class<?>, Format> entry : USER_REGISTERED_FORMATS.entrySet()) {
            formatMap.put(entry.getKey(), entry.getValue());
        }
    }

    public void addFormat(Class<?> targetClass, Format format) {
        formatMap.put(targetClass, format);
    }

    public Format findFormatFor(Class<?> targetClass) {
        if (!formatMap.containsKey(targetClass)) {
            throw new RuntimeException("Could not find targetClass " + targetClass.getName() + " in format registry");
        }
        return (Format) formatMap.get(targetClass);
    }
}