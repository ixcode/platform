package ixcode.platform.reflect;

import java.util.*;

import static ixcode.platform.reflect.ObjectReflector.reflect;

public class ObjectBuilder {

    private final ObjectReflector objectReflector;


    private Map<String, String> propertyValues = new HashMap<String, String>();

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

    public Class<?> getTypeOfCollectionCalled(String propertyName) {
        return objectReflector.typeOfCollectionProperty(propertyName);
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

        public void asObject(Object propertyValueAsObject) {
            parent.addPropertyAsObject(propertyName, propertyValueAsObject);
        }
    }

    private void addPropertyAsObject(String propertyName, Object propertyValueAsObject) {

    }

    private void addProperty(String propertyName, String propertyValue) {
        propertyValues.put(propertyName, propertyValue);
    }
}