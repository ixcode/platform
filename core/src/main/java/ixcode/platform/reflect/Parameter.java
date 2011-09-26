package ixcode.platform.reflect;

import java.util.*;

public class Parameter {
    public static Class[] extractParameterClasses(Parameter[] parameters) {
        List<Class> parameterClasses = new ArrayList<Class>();
        for (Parameter parameter : parameters) {
            parameterClasses.add(parameter.argClass);
        }
        return parameterClasses.toArray(new Class[parameters.length]);
    }

    public static Object[] extractArgValues(Parameter[] parameters) {
        List<Object> parameterValues = new ArrayList<Object>();
        for (Parameter parameter : parameters) {
            parameterValues.add(parameter.argValue);
        }
        return parameterValues.toArray(new Object[parameters.length]);
    }

    public static Parameter parameter(Class declaredParameterClass, Object argValue) {
        return new Parameter(declaredParameterClass, argValue);
    }

    public final Class argClass;
    public final Object argValue;

    public Parameter(Class argClass, Object argValue) {
        this.argClass = argClass;
        this.argValue = argValue;
    }
}