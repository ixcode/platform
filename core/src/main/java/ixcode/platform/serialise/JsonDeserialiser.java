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

import static java.lang.String.format;

public class JsonDeserialiser {


    private KindToClassMap kindToClassMap;

    public JsonDeserialiser(KindToClassMap kindToClassMap) {
        this.kindToClassMap = kindToClassMap;
    }


    public <T> T deserialise(String json) {
        JsonObject jsonObject = new JsonParser().parse(json);
        Class<?> type = kindToClassMap.classFor(getKindFrom(jsonObject));

        return parseJsonObject(jsonObject, type);
    }


    protected void parseValue(ObjectBuilder objectBuilder, String key, Object value) {
        if (value instanceof String) {
            populateProperty(objectBuilder, key, (String) value);
        } if (value instanceof JsonArray) {
            populateJsonArray(objectBuilder, key, (JsonArray) value);
        }
    }

    protected void populateProperty(ObjectBuilder objectBuilder, String propertyName, String value) {
        objectBuilder.setProperty(propertyName).fromString(value);
    }


    private void populateJsonArray(ObjectBuilder objectBuilder, String propertyName, JsonArray jsonArray) {
        final Class<?> typeOfChildren = objectBuilder.getTypeOfCollectionCalled(propertyName);

        final List<Object> transformedObjects = new ArrayList<Object>();
        jsonArray.apply(new Action<Object>() {
            @Override public void to(Object item, Collection<Object> tail) {

                if (item instanceof JsonObject) {
                    transformedObjects.add(parseJsonObject((JsonObject) item, typeOfChildren));
                } else {
                    throw new RuntimeException(format("Ah, can't work out what to do with a [%s]", item.getClass()));
                }

            }
        });

        objectBuilder.setProperty(propertyName).asObject(transformedObjects);

    }


    private <T> T parseJsonObject(JsonObject jsonObject, Class<?> type) {
        final ObjectBuilder objectBuilder = new ObjectBuilder(type);

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
            return (String) value;
        }
        return jsonObject.<List<String>>valueOf("is").get(0);
    }


}