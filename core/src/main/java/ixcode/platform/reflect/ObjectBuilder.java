package ixcode.platform.reflect;

import java.util.*;

public class ObjectBuilder {

    private final ObjectReflector objectReflector;


    private Map<String, String> propertyValues = new HashMap<String, String>();

    public ObjectBuilder(Class<?> rootEntityClass) {
        this.objectReflector = new ObjectReflector(rootEntityClass);
    }

    public <T> T build() {
        return objectReflector.invokeMostSpecificConstructorFor(propertyValues);
    }

    public PropertyBuilder setProperty(String propertyName) {
        return new PropertyBuilder(this, propertyName);
    }

    public static class PropertyBuilder {

        private ObjectBuilder parent;
        private String propertyName;
        private String propertyValue;

        public PropertyBuilder(ObjectBuilder parent, String propertyName) {
            this.parent = parent;
            this.propertyName = propertyName;
        }

        public void fromString(String propertyValue) {
            parent.addProperty(propertyName, propertyValue);
        }
    }

    private void addProperty(String propertyName, String propertyValue) {
        propertyValues.put(propertyName, propertyValue);
    }
}