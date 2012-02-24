package ixcode.platform.di;

import ixcode.platform.reflect.ObjectFactory;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

public class InjectionContext {

    private static final Logger log = Logger.getLogger(InjectionContext.class);

    private Map<Class<?>, Object> context = new HashMap<Class<?>, Object>();

    public <T> T getA(Class<T> targetClass) {
        if (!context.containsKey(targetClass)) {
            log.debug(noSingletonMessage(targetClass));
            return new ObjectFactory<T>().instantiate(targetClass);
        }
        return (T) context.get(targetClass);
    }

    public <T> T getThe(Class<T> targetClass) {
        if (!context.containsKey(targetClass)) {
            throw new RuntimeException(noSingletonMessage(targetClass));
        }
        return (T) context.get(targetClass);
    }

    public void register(Object object) {
        context.put(object.getClass(), object);
    }




    private static <T> String noSingletonMessage(Class<T> targetClass) {
        return format("No singleton injection context registered for [%s]", targetClass.getName());
    }
}