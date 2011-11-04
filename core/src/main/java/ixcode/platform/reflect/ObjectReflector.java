package ixcode.platform.reflect;

import ixcode.platform.collection.*;
import ixcode.platform.text.format.Format;
import ixcode.platform.text.format.FormatRegistry;
import ixcode.platform.text.format.StringToObjectParser;

import java.lang.reflect.*;
import java.util.*;

import static java.lang.reflect.Modifier.isTransient;

public class ObjectReflector {
    public final FList<FieldReflector> nonTransientFields;

    private final Class<?> targetClass;
    private final ConstructorMatrix constructorMatrix;
    private final StringToObjectParser parser = new StringToObjectParser();
    private final FormatRegistry formatRegistry = new FormatRegistry();

    public static ObjectReflector reflect(Class<?> targetClass) {
        return new ObjectReflector(targetClass);
    }

    private ObjectReflector(Class<?> targetClass) {
        this.targetClass = targetClass;
        this.constructorMatrix = new ConstructorMatrix(targetClass);
        this.nonTransientFields = extractNonTransientFields(targetClass);
    }

    public <T> T invokeMostSpecificConstructorFor(Map<String, String> valueMap) {
        ParameterSet parameterSet = constructorMatrix.findMostSpecificMatchTo(valueMap.keySet());

        List<Object> values = new ArrayList<Object>();

        for (ParameterSet.ParameterDefinition definition : parameterSet.parameterDefinitions) {
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

    private FList<FieldReflector> extractNonTransientFields(Class<?> targetClass) {
        FList<FieldReflector> fieldReflectors = new FArrayList<FieldReflector>();
        for (Field field : targetClass.getDeclaredFields()) {
            if (!isTransient(field.getModifiers())) {
                fieldReflectors.add(new FieldReflector(field));
            }
        }
        return fieldReflectors;
    }

    public Map<String, String> propertyValuesOf(final Object anInstance) {
        final Map<String, String> propertyValues = new LinkedHashMap<String, String>();
        nonTransientFields.apply(new Action<FieldReflector>() {
            @Override public void to(FieldReflector item, Collection<FieldReflector> tail) {
                Object value = item.valueFrom(anInstance);
                if (value != null) {
                    Format format = formatRegistry.findFormatFor(value.getClass());
                    propertyValues.put(item.name, format.format(value));
                }
            }
        });
        return propertyValues;
    }

    public void withEachNonTransientField(Action<FieldReflector> action) {
        this.nonTransientFields.apply(action);
    }


}