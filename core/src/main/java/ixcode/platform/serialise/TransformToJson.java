package ixcode.platform.serialise;

import ixcode.platform.collection.Action;
import ixcode.platform.json.JsonArray;
import ixcode.platform.json.JsonObject;
import ixcode.platform.json.JsonObjectBuilder;
import ixcode.platform.reflect.FieldReflector;
import ixcode.platform.reflect.ObjectReflector;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ixcode.platform.json.JsonObjectBuilder.jsonObjectWith;
import static ixcode.platform.reflect.ObjectReflector.reflect;
import static ixcode.platform.reflect.TypeChecks.isList;
import static ixcode.platform.reflect.TypeChecks.isMap;

public class TransformToJson {
    public <T, R> R from(T object) {
        return (isList(object))
                ? (R) buildJsonArrayFrom((Collection<?>) object)
                : (R) jsonValueOf(null, object);
    }

    protected JsonObject buildJsonObjectFrom(final Object object) {
        final Map<String, Object> valueMap = new LinkedHashMap<String, Object>();

        ObjectReflector objectReflector = reflect(object.getClass());
        objectReflector.nonTransientFields.apply(new Action<FieldReflector>() {
            @Override public void to(FieldReflector item, Collection<FieldReflector> tail) {
                Object value = item.valueFrom(object);
                if (value != null) {
                    valueMap.put(item.name, jsonValueOf(JsonObject.class, value));
                }
            }
        });

        return new JsonObject(valueMap);
    }

    protected JsonObject buildJsonObjectFrom(final Map<?, ?> object) {
        JsonObjectBuilder jsonObjectBuilder = jsonObjectWith();;
        for (Map.Entry<?, ?> entry : object.entrySet()) {
            jsonObjectBuilder.key(entry.getKey().toString()).value(jsonValueOf(JsonObject.class, entry.getValue()));
        }
        return jsonObjectBuilder.build();
    }

    private JsonArray buildJsonArrayFrom(Collection<?> collection) {
        List<Object> items = new ArrayList<Object>();

        for (Object object : collection) {
            items.add(jsonValueOf(List.class, object));
        }

        return new JsonArray(items);
    }

    protected Object jsonValueOf(Class<?> parentType, Object value) {
        if (value == null) {
            return null;
        }

        Class<?> valueClass = value.getClass();

        if (isMap(valueClass)) {
            return buildJsonObjectFrom((Map) value);
        } else if (Collection.class.isAssignableFrom(valueClass)) {
            return buildJsonArrayFrom((List) value);
        } else if (Integer.class.isAssignableFrom(valueClass)
                || Long.class.isAssignableFrom(valueClass)
                || Double.class.isAssignableFrom(valueClass)
                || String.class.isAssignableFrom(valueClass)
                || Boolean.class.isAssignableFrom(valueClass)
                || UUID.class.isAssignableFrom(valueClass)
                || URI.class.isAssignableFrom(valueClass)
                || URL.class.isAssignableFrom(valueClass)
                || Date.class.isAssignableFrom(valueClass)) {
            return value;
        }
        return buildJsonObjectFrom(value);
    }

    public static String jsonObjectNameFor(Object object) {
        String simpleName = object.getClass().getSimpleName();
        return String.format("%s%s", simpleName.substring(0, 1).toLowerCase(), simpleName.substring(1));
    }




}