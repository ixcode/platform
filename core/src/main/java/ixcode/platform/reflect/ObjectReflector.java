package ixcode.platform.reflect;

import ixcode.platform.collection.Action;
import ixcode.platform.collection.FArrayList;
import ixcode.platform.collection.FList;
import ixcode.platform.text.format.Format;
import ixcode.platform.text.format.FormatRegistry;
import ixcode.platform.text.format.StringToObjectParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.isTransient;

public class ObjectReflector {
    public final FList<FieldReflector> nonTransientFields;


    private final Class<?> targetClass;
    private final ConstructorMatrix constructorMatrix;
    private final StringToObjectParser parse = new StringToObjectParser();
    private final FormatRegistry formatRegistry = new FormatRegistry();
    private final Map<String, FieldReflector> fieldMap = new LinkedHashMap<String, FieldReflector>();

    public static ObjectReflector reflect(Class<?> targetClass) {
        return new ObjectReflector(targetClass);
    }

    private ObjectReflector(Class<?> targetClass) {
        this.targetClass = targetClass;
        this.constructorMatrix = new ConstructorMatrix(targetClass);
        this.nonTransientFields = processFields(targetClass);
    }

    public <T> T invokeMostSpecificConstructorFor(Map<String, Object> valueMap) {
        ParameterSet parameterSet = constructorMatrix.findMostSpecificMatchTo(valueMap.keySet());

        List<Object> values = new ArrayList<Object>();

        for (ParameterSet.Parameter parameter : parameterSet.parameters) {
            values.add(parseObject(valueMap, parameter));
        }

        try {
            parameterSet.constructor.setAccessible(true);
            return (T) parameterSet.constructor.newInstance(values.toArray(new Object[0]));
        } catch (Exception e) {
            throw new RuntimeException("Could not invoke most specific constructor (See Cause)", e);
        }
    }

    private Object parseObject(Map<String, Object> valueMap, ParameterSet.Parameter definition) {
        Object source = valueMap.get(definition.name);
        return (source instanceof String)
                ? parse.fromString((String)source).as(definition.type)
                : source;
    }

    public Class<?> typeOfCollectionField(String fieldName) {
        if (!fieldMap.containsKey(fieldName)) {
            throw new RuntimeException("No field called " + fieldName);
        }
        FieldReflector fieldReflector = fieldMap.get(fieldName);
        if (fieldReflector.isCollection()) {
            return (Class<?>) fieldReflector.genericTypeArguments()[0];
        }
        throw new RuntimeException(format("Oh dear, the field [%s] is not a collection it's a [%s]", fieldReflector.type));
    }

    public boolean hasField(String propertyName) {
        return fieldMap.containsKey(propertyName);
    }

    public Map<String, String> fieldValuesOf(final Object anInstance) {
        final Map<String, String> fieldValues = new LinkedHashMap<String, String>();
        nonTransientFields.apply(new Action<FieldReflector>() {
            @Override public void to(FieldReflector item, Collection<FieldReflector> tail) {
                Object value = item.valueFrom(anInstance);
                if (value != null) {
                    Format format = formatRegistry.findFormatFor(value.getClass());
                    fieldValues.put(item.name, format.format(value));
                }
            }
        });
        return fieldValues;
    }

    public void withEachNonTransientField(Action<FieldReflector> action) {
        this.nonTransientFields.apply(action);
    }

    public void withEachFieldHavingAnnotation(final Class<? extends Annotation> annotation,
                                              Action<FieldReflector> action) {

        final FList<FieldReflector> withAnnotationList = new FArrayList<FieldReflector>();

        withEachNonTransientField(new Action<FieldReflector>() {
            @Override public void to(FieldReflector item, Collection<FieldReflector> tail) {
               if (item.hasAnnotation(annotation)) {
                   withAnnotationList.add(item);
               }
            }
        });

        withAnnotationList.apply(action);
    }


    public Format findFormatFor(Class<? extends Object> aClass) {
        return formatRegistry.findFormatFor(aClass);
    }



    private FList<FieldReflector> processFields(Class<?> targetClass) {
        FList<FieldReflector> nonTransientFields = new FArrayList<FieldReflector>();
        for (Field field : targetClass.getDeclaredFields()) {
            FieldReflector fieldReflector = new FieldReflector(field);
            fieldMap.put(field.getName(), fieldReflector);

            if (!isTransient(field.getModifiers())) {

                nonTransientFields.add(fieldReflector);
            }
        }
        return nonTransientFields;
    }


}