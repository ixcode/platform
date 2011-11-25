package ixcode.platform.serialise;

import ixcode.platform.collection.Action;
import ixcode.platform.json.JsonArray;
import ixcode.platform.json.JsonObject;
import ixcode.platform.json.JsonPair;
import ixcode.platform.json.JsonParser;
import ixcode.platform.reflect.ObjectBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JsonDeserialiser {


    private KindToClassMap kindToClassMap;

    public JsonDeserialiser(KindToClassMap kindToClassMap) {
        this.kindToClassMap = kindToClassMap;
    }



    public <T> T deserialise(String json) {
        JsonObject jsonObject = new JsonParser().parse(json);

        String kind = getKindFrom(jsonObject);

        final ObjectBuilder objectBuilder = new ObjectBuilder(kindToClassMap.classFor(kind));

        jsonObject.apply(new Action<JsonPair>() {
            @Override public void to(JsonPair item, Collection<JsonPair> tail) {
                if (!item.key.equals("is")) {
                    parseValue(objectBuilder, item.key, item.value);
                }

            }
        });

        return objectBuilder.build();
    }

    private String getKindFrom(JsonObject jsonObject) {
        Object value = jsonObject.valueOf("is");
        if (value instanceof String) {
            return (String)value;
        }
        return jsonObject.<List<String>>valueOf("is").get(0);
    }


    protected void parseValue(ObjectBuilder objectBuilder, String key, Object value) {
        if (value instanceof String) {
            populateProperty(objectBuilder, key, (String) value);
        } if (value instanceof JsonArray) {
            populateJsonArray(key, (JsonArray) value);
        }
    }

    protected void populateProperty(ObjectBuilder objectBuilder, String propertyName, String value) {
        objectBuilder.setProperty(propertyName).fromString(value);
    }


    private void populateJsonArray(String propertyName, JsonArray jsonArray) {
        final List<Object> transformedObjects = new ArrayList<Object>();
        jsonArray.apply(new Action<Object>() {
            @Override public void to(Object item, Collection<Object> tail) {
                transformedObjects.add(parseObject(item));
            }


        });

    }

    private Object parseObject(Object object) {
        return null;
    }
}