package ixcode.platform.reflect;

import java.lang.reflect.*;

public class FieldReflector {
    public final String name;
    public final Class<?> type;

    private final Field field;

    public FieldReflector(String name, Field field) {
        this.name = name;
        this.field = field;
        this.type = field.getType();
    }

    public <T> T valueFrom(Object parent) {
        try {
            return (T)field.get(parent);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("Could not get field value [%s] from [%s] (See Cause)", name, parent, e));
        }
    }
}