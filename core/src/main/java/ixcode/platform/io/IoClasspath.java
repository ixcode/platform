package ixcode.platform.io;

import java.io.InputStream;

import static java.lang.Thread.currentThread;

public class IoClasspath {
    
    public static InputStream inputStreamFromClasspathEntry(String classpathEntry) {
        return currentThread().getContextClassLoader().getResourceAsStream(classpathEntry);
    }

    public static InputStream inputStreamFromClasspathEntry(Object location, String classpathEntry) {
        return inputStreamFromClasspathEntry(location.getClass(), classpathEntry);
    }
    public static InputStream inputStreamFromClasspathEntry(Class<?> location, String classpathEntry) {
        return location.getClassLoader().getResourceAsStream(classpathEntry);
    }
    
}