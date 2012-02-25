package ixcode.platform.io;

import java.io.InputStream;

import static java.lang.Thread.currentThread;

public class IoClasspath {
    
    public static InputStream inputStreamFromClasspathEntry(String classpathEntry) {
        return currentThread().getContextClassLoader().getResourceAsStream(classpathEntry);
    }
    
}