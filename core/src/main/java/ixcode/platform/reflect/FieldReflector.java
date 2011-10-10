package ixcode.platform.reflect;

import java.lang.reflect.*;

import static java.lang.String.format;

public class FieldReflector {
    public final String name;
    public final Class<?> type;

    private final Field field;

    public FieldReflector(Field field) {
        this.field = field;
        this.name = this.field.getName();
        this.type = this.field.getType();
        this.field.setAccessible(true);
    }

    public <T> T valueFrom(Object parent) {
        try {
            return (T)field.get(parent);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(format("Could not get field value [%s] from [%s] (See Cause)", name, parent), e);
        }
    }
}