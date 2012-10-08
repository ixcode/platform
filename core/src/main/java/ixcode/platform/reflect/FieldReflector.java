package ixcode.platform.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

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

    public boolean hasAnnotation(Class<? extends Annotation> annotationClass) {
        return this.field.isAnnotationPresent(annotationClass);
    }

    public Type[] genericTypeArguments() {
        return ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
    }

    public boolean isCollection() {
        return Collection.class.isAssignableFrom(type);
    }

    public boolean isMap() {
        return Map.class.isAssignableFrom(type);
    }

    public Class<?> type() {
        return field.getType();
    }


}