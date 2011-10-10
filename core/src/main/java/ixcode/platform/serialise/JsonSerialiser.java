package ixcode.platform.serialise;

import ixcode.platform.collection.*;
import ixcode.platform.json.*;
import ixcode.platform.reflect.*;
import ixcode.platform.text.*;

import java.lang.reflect.*;
import java.util.*;

import static ixcode.platform.reflect.ObjectReflector.reflect;
import static ixcode.platform.reflect.TypeChecks.*;

public class JsonSerialiser {

    private final FormatRegistry formatRegistry = new FormatRegistry();
    private final JsonPrinter printer = new FlatJsonPrinter();

    public <T> String toJson(T object) {
        Object root = (isCollection(object))
                ? buildJsonArrayFrom(object)
                : buildJsonObjectFrom(object);

        return printer.print(root);
    }

    private Object buildJsonArrayFrom(Object object) {
        List<Object> items = new ArrayList<Object>();

        items.add(buildJsonObjectFrom(object));

        return new JsonArray(items);
    }

    private Object buildJsonObjectFrom(final Object object) {
        final Map<String, Object> valueMap = new LinkedHashMap<String, Object>();

        ObjectReflector objectReflector = reflect(object.getClass());
        objectReflector.nonTransientFields.apply(new Action<FieldReflector>() {
            @Override public void to(FieldReflector item, Collection<FieldReflector> tail) {
                valueMap.put(item.name, formatFieldValue(item, object));
            }
        });

        return new JsonObject(valueMap);
    }

    private String formatFieldValue(FieldReflector fieldReflector, Object object) {
        return formatRegistry.findFormatFor(fieldReflector.type)
                             .formatObject(fieldReflector.valueFrom(object));
    }


}