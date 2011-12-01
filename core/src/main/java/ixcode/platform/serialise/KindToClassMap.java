package ixcode.platform.serialise;

import java.util.LinkedHashMap;
import java.util.Map;

public class KindToClassMap {


    private Map<String, Class<?>> map;

    public static KindToClassMapBuilder map() {
        return new KindToClassMapBuilder();
    }

    public Class<?> classFor(String kind) {
        return map.get(kind);
    }

    private KindToClassMap(Map<String, Class<?>> map) {
        this.map = map;
    }


    public static class KindToClassMapBuilder {

        Map<String, Class<?>> map = new LinkedHashMap<String, Class<?>>();

        public KindToClassEntryBuilder kind(String kind) {
            return new KindToClassEntryBuilder(kind, this);
        }

        public KindToClassMap build() {
            return new KindToClassMap(map);
        }

        public void registerMapping(String kind, Class<?> targetClass) {
            map.put(kind, targetClass);
        }

        public KindToClassMapBuilder merge(KindToClassMap kindToClassMap) {
            for (Map.Entry<String, Class<?>> entry : kindToClassMap.map.entrySet()) {
                map.put(entry.getKey(), entry.getValue());
            }
            return this;
        }
    }

    public static class KindToClassEntryBuilder {

        private final String kind;
        private final KindToClassMapBuilder parent;

        public KindToClassEntryBuilder(String kind, KindToClassMapBuilder parent) {
            this.kind = kind;
            this.parent = parent;
        }

        public KindToClassMapBuilder to(Class<?> targetClass) {
            parent.registerMapping(kind, targetClass);
            return parent;
        }
    }
}