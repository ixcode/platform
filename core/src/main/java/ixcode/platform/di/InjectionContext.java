package ixcode.platform.di;

import java.util.HashMap;
import java.util.Map;

public class InjectionContext {

    private Map<Class<?>, Object> context = new HashMap<Class<?>, Object>();

    public <T> T getA(Class<T> targetClass) {
        return (T) context.get(targetClass);
    }

    public void register(Object object) {
        context.put(object.getClass(), object);
    }
}