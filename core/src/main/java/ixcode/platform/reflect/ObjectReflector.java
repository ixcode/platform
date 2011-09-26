package ixcode.platform.reflect;

import ixcode.platform.reflect.ConstructorMatrix.*;

import ixcode.platform.text.*;

import java.lang.reflect.*;
import java.util.*;

public class ObjectReflector {
    private Class<?> targetClass;
    private ConstructorMatrix constructorMatrix;
    private StringToObjectParser parser = new StringToObjectParser();;

    public ObjectReflector(Class<?> targetClass) {
        this.targetClass = targetClass;
        this.constructorMatrix = new ConstructorMatrix(targetClass);
    }

    public <T> T invokeMostSpecificConstructorFor(Map<String, String> valueMap) {
        ParameterSet parameterSet = constructorMatrix.findMostSpecificMatchTo(valueMap.keySet());

        List<Object> values = new ArrayList<Object>();

        for (ParameterDefinition definition : parameterSet.parameterDefinitions) {
            Object value = parser.parse(valueMap.get(definition.name)).as(definition.type);
            values.add(value);
        }

        try {
            parameterSet.constructor.setAccessible(true);
            return (T) parameterSet.constructor.newInstance(values.toArray(new Object[0]));
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}