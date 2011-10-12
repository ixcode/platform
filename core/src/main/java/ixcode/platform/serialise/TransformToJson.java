package ixcode.platform.serialise;

import ixcode.platform.collection.Action;
import ixcode.platform.json.JsonArray;
import ixcode.platform.json.JsonObject;
import ixcode.platform.reflect.FieldReflector;
import ixcode.platform.reflect.ObjectReflector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ixcode.platform.reflect.ObjectReflector.reflect;
import static ixcode.platform.reflect.TypeChecks.isCollection;

public class TransformToJson {
    public <T, R> R from(T object) {
        return (isCollection(object))
                ? (R) buildJsonArrayFrom((Collection<?>)object)
                : (R) buildJsonObjectFrom(object);
    }

    protected JsonObject buildJsonObjectFrom(final Object object) {
        final Map<String, Object> valueMap = new LinkedHashMap<String, Object>();

        ObjectReflector objectReflector = reflect(object.getClass());
        objectReflector.nonTransientFields.apply(new Action<FieldReflector>() {
            @Override public void to(FieldReflector item, Collection<FieldReflector> tail) {
                valueMap.put(item.name, item.valueFrom(object));
            }
        });

        return new JsonObject(valueMap);
    }


    private JsonArray buildJsonArrayFrom(Collection<?> object) {
        List<Object> items = new ArrayList<Object>();

        items.add(buildJsonObjectFrom(object));

        return new JsonArray(items);
    }


}