package ixcode.platform.io;

import java.io.InputStream;

import static ixcode.platform.exception.NullValueException.notNull;
import static java.lang.String.format;
import static java.lang.Thread.currentThread;

public class IoClasspath {

    public static InputStream inputStreamFromClasspathEntry(String classpathEntry) {
        return currentThread().getContextClassLoader().getResourceAsStream(classpathEntry);
    }

    public static InputStream inputStreamFromClasspathEntry(Object location, String classpathEntry) {
        return inputStreamFromClasspathEntry(location.getClass(), classpathEntry);
    }

    public static InputStream inputStreamFromClasspathEntry(Class<?> location, String classpathEntry) {
        return notNull(location.getResourceAsStream(classpathEntry),
                       format("Could not find classpath entry [%s]", fqClasspathEntry(location, classpathEntry)));

    }

    private static String fqClasspathEntry(Class<?> location, String classpathEntry) {
        return format("%s/%s", location.getName().replaceAll("\\.", "/"), classpathEntry);
    }

}