package ixcode.platform.reflect;

import java.util.*;

public class TypeChecks {
    public static <T> boolean isCollection(T object) {
        return Collection.class.isAssignableFrom(object.getClass());
    }
}