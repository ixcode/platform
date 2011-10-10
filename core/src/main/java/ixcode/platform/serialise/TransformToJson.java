package ixcode.platform.serialise;

import ixcode.platform.collection.*;
import ixcode.platform.json.*;
import ixcode.platform.reflect.*;

import java.util.*;

import static ixcode.platform.reflect.ObjectReflector.*;
import static ixcode.platform.reflect.TypeChecks.*;

class TransformToJson {
    public <T, R> R from(T object) {
        return (isCollection(object))
                ? (R)buildJsonArrayFrom(object)
                : (R)buildJsonObjectFrom(object);
    }

    private static Object buildJsonArrayFrom(Object object) {
        List<Object> items = new ArrayList<Object>();

        items.add(buildJsonObjectFrom(object));

        return new JsonArray(items);
    }

    private static Object buildJsonObjectFrom(final Object object) {
        final Map<String, Object> valueMap = new LinkedHashMap<String, Object>();

        ObjectReflector objectReflector = reflect(object.getClass());
        objectReflector.nonTransientFields.apply(new Action<FieldReflector>() {
            @Override public void to(FieldReflector item, Collection<FieldReflector> tail) {
                valueMap.put(item.name, item.valueFrom(object));
            }
        });

        return new JsonObject(valueMap);
    }

}