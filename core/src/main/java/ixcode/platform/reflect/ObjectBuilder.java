package ixcode.platform.reflect;

import java.util.*;

import static ixcode.platform.reflect.ObjectFactory.loadClass;
import static ixcode.platform.reflect.ObjectReflector.reflect;

public class ObjectBuilder {

    private final ObjectReflector objectReflector;


    private Map<String, Object> propertyValues = new LinkedHashMap<String, Object>();

    public static ObjectBuilder buildA(String fqClassName) {
        return buildA(loadClass(fqClassName));
    }

    public static ObjectBuilder buildA(Class<?> rootEntityClass) {
        return new ObjectBuilder(rootEntityClass);
    }

    public ObjectBuilder(Class<?> rootEntityClass) {
        this.objectReflector = reflect(rootEntityClass);
    }

    public <T> T build() {
        return objectReflector.invokeMostSpecificConstructorFor(propertyValues);
    }

    public PropertyBuilder setProperty(String propertyName) {
        return new PropertyBuilder(this, propertyName);
    }

    public Object getPropertyValue(String key) {
        return propertyValues.get(key);
    }

    public Class<?> getPropertyType(String key) {
        return objectReflector.getFieldType(key);
    }

    public Class<?> getTypeOfCollectionCalled(String propertyName) {
        return objectReflector.typeOfCollectionField(propertyName);
    }

    private void addProperty(String propertyName, Object propertyValue) {
        propertyValues.put(propertyName, propertyValue);
    }

    public boolean hasProperty(String propertyName) {
        return objectReflector.hasField(propertyName);
    }

    public boolean isCollection(String propertyName) {
        return objectReflector.isCollection(propertyName);
    }

    public boolean isMap(String propertyName) {
        return objectReflector.isMap(propertyName);
    }

    public boolean isList(String propertyName) {
        return objectReflector.isList(propertyName);
    }

    public boolean isArray(String propertyName) {
        return objectReflector.isArray(propertyName);
    }



    public static class PropertyBuilder {

        private ObjectBuilder parent;
        private String propertyName;

        public PropertyBuilder(ObjectBuilder parent, String propertyName) {
            this.parent = parent;
            this.propertyName = propertyName;
        }

        public ObjectBuilder fromString(String propertyValue) {
            parent.addProperty(propertyName, propertyValue);
            return parent;
        }

        public ObjectBuilder asObject(Object propertyValueAsObject) {
            parent.addProperty(propertyName, propertyValueAsObject);
            return parent;
        }
    }

}