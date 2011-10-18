package ixcode.platform.http.representation;

import ixcode.platform.collection.MapBuilder;
import ixcode.platform.reflect.FieldReflector;
import ixcode.platform.reflect.ObjectReflector;
import sun.security.krb5.internal.KdcErrException;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static ixcode.platform.collection.MapBuilder.linkedHashMapWith;
import static ixcode.platform.reflect.ObjectReflector.reflect;

public class ReflectiveMapBuilder  extends MapBuilder<String, Object> {

    public ReflectiveMapBuilder() {
        super(new LinkedHashMap<String, Object>());
    }

    public ReflectiveMapBuilder extractValuesFrom(Object source) {
        ObjectReflector reflector = reflect(source.getClass());
        for (FieldReflector fieldReflector : reflector.nonTransientFields) {
            Object value = fieldReflector.valueFrom(source);
            if (value != null) {
                key(fieldReflector.name).value(value);
            }
        }
        return this;
    }
}