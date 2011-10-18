package ixcode.platform.text.format;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormatRegistry {

    private final Map<Class<?>, Format> formatMap = new HashMap<Class<?>, Format>();

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