package ixcode.platform.serialise;

import ixcode.platform.collection.Action;
import ixcode.platform.json.JsonArray;
import ixcode.platform.json.JsonObject;
import ixcode.platform.json.JsonPair;
import ixcode.platform.json.JsonParser;
import ixcode.platform.reflect.ObjectBuilder;
import ixcode.platform.text.format.FormatRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonDeserialiser {


    private KindToClassMap kindToClassMap;
    private final FormatRegistry formatRegistry = new FormatRegistry();

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
        } else if (value instanceof JsonArray) {
            populateJsonArray(objectBuilder, key, (JsonArray) value);
        } else if (value instanceof JsonObject) {
            if (kindToClassMap.canMap(key)) {
                objectBuilder.setProperty(key).asObject(parseJsonObject((JsonObject) value, kindToClassMap.classFor(key)));
            } else {
                Map<String, Object> map = parseMapFrom((JsonObject)value);
                objectBuilder.setProperty(key).asObject(map);
            }
        } else {
            objectBuilder.setProperty(key).asObject(value);
        }
    }

    private static Map<String, Object> parseMapFrom(JsonObject jsonObject) {
        final Map<String, Object> map = new HashMap<String, Object>();

        jsonObject.apply(new Action<JsonPair>() {
            @Override public void to(JsonPair item, Collection<JsonPair> tail) {
               if (item.value instanceof JsonObject) {
                   map.put(item.key, parseMapFrom((JsonObject) item.value));
               } else if (item.value instanceof JsonArray) {
                   List<Object> list = parseListFrom((JsonArray) item.value);
                   map.put(item.key, list);
               } else {
                   map.put(item.key, item.value);
               }
            }
        });

        return map;
    }

    private static List<Object> parseListFrom(JsonArray jsonArray) {
        final List<Object> list = new ArrayList<Object>();

        jsonArray.apply(new Action<Object>() {
            @Override public void to(Object item, Collection<Object> tail) {
                if (item instanceof JsonObject) {
                    list.add(parseMapFrom((JsonObject)item));
                } else if (item instanceof JsonArray) {
                    list.add(parseListFrom((JsonArray)item));
                } else {
                    list.add(item);
                }
            }
        });

        return list;
    }

    protected void populateProperty(ObjectBuilder objectBuilder, String propertyName, String value) {
        objectBuilder.setProperty(propertyName).fromString(value);
    }


    private void populateJsonArray(ObjectBuilder objectBuilder, String propertyName, JsonArray jsonArray) {
        if (!objectBuilder.hasProperty(propertyName)) {
            return;
        }
        final Class<?> typeOfChildren = objectBuilder.getTypeOfCollectionCalled(propertyName);

        final List<Object> transformedObjects = new ArrayList<Object>();
        jsonArray.apply(new Action<Object>() {
            @Override public void to(Object item, Collection<Object> tail) {

                if (item instanceof JsonObject) {
                    transformedObjects.add(parseJsonObject((JsonObject) item, typeOfChildren));
                } else if (item instanceof String) {
                    transformedObjects.add(formatRegistry.findFormatFor(typeOfChildren).parseString((String) item));
                } else {
                    throw new RuntimeException("Ah, don't know how to parse object: " + item);
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
        return jsonObject.<JsonArray>valueOf("is").get(0);
    }


}