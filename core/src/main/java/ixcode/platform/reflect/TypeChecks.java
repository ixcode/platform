package ixcode.platform.reflect;

import java.util.*;

public class TypeChecks {
    public static <T> boolean isList(T object) {
        return List.class.isAssignableFrom(object.getClass());
    }

    public static <T> boolean isMap(Class<T> objectClass) {
        return Map.class.isAssignableFrom(objectClass);
    }
}