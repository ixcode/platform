package ixcode.platform.reflect;

import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static java.lang.String.format;
import static java.lang.Thread.currentThread;

public class ObjectFactory<T> {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ObjectFactory.class);

    public static boolean isClassAvailable(String fullyQualifiedName) {
        try {
            currentThread().getContextClassLoader().loadClass(fullyQualifiedName);
        } catch (ClassNotFoundException e) {
            return false;
        } catch (NoClassDefFoundError e) {
            return false;
        }

        return true;
    }

    public static Class<?> loadClass(String fullyQualifiedName) {
        try {
            return currentThread().getContextClassLoader().loadClass(fullyQualifiedName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(format("Could not load class [%s] from classloader [%s]",fullyQualifiedName, currentThread().getContextClassLoader()), e);
        }
    }


    public T instantiate(String className) {
        try {
            return instantiate((Class<T>)loadClass(className));
        } catch (Exception e) {
            throw new RuntimeException(format("Could not instantiate [%s] (see cause)", className), e);
        }
    }

    public T instantiate(Class<T> classToInstantiate) {
        try {
            Constructor constructor = classToInstantiate.getConstructor();
            constructor.setAccessible(true);
            return (T) constructor.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public T instantiateWithDefault(String classname, T defaultInstance) {
        return instantiateWithDefault(classname, defaultInstance, true);
    }

    public T instantiateWithDefault(String classname, T defaultInstance, boolean shouldLogToError) {
        try {
            return (T) loadClass(classname).newInstance();
        } catch (Exception e) {
            logNotInstantiated(classname, e, shouldLogToError);
            return defaultInstance;
        }
    }

    public T instantiateWithArg(Class classToInstantiate, Class argClass, Object arg) {
        return instantiateWithArgs(classToInstantiate, Parameter.parameter(argClass, arg));
    }

    public T instantiateWithArgs(Class classToInstantiate, Parameter... parameters) {
        Class[] argTypes = Parameter.extractParameterClasses(parameters);
        Object[] argValues = Parameter.extractArgValues(parameters);
        try {
            Constructor constructor = classToInstantiate.getConstructor(argTypes);
            constructor.setAccessible(true);
            return (T) constructor.newInstance(argValues);
        } catch (Exception e) {
            logNotInstantiated(classToInstantiate.getName(), e);
            return null;
        }
    }

    public T instantiateWithArg(String classname, Class argClass, Object arg) {
        return instantiateWithArg(classname, argClass, arg, true);
    }

    public T instantiateWithArg(String classname, Class argClass, Object arg, boolean shouldLogToError) {
        try {
            Class classToInstantiate = loadClass(classname);
            return instantiateWithArg(classToInstantiate, argClass, arg);
        } catch (Exception e) {
            logNotInstantiated(classname, e, shouldLogToError);
            return null;
        }
    }

    private void logNotInstantiated(String classname, Exception e) {
        logNotInstantiated(classname, e, true);
    }

    private void logNotInstantiated(String classname, Exception e, boolean shouldLogToError) {
        if (shouldLogToError) {
            log.error("Did not instantiate class of name [" + classname + "]", e);
        }
    }



}
