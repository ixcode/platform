package ixcode.platform.collection;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapBuilder<K, V> {

    private Map<K, V> map;

    public MapBuilder(Map<K, V> map) {
        this.map = map;
    }

    public static <K, V> MapBuilder linkedHashMapWith() {
        return new MapBuilder(new LinkedHashMap<K, V>());
    }

    public EntryBuilder<K, V> key(K key) {
        return new EntryBuilder<K, V>(this, key);
    }

    private void addEntry(K key, V value) {
        map.put(key, value);
    }

    public Map<K, V> build() {
        return map;
    }

    public static class EntryBuilder<K, V> {

        private final MapBuilder<K, V> parent;
        private final K key;

        public EntryBuilder(MapBuilder<K, V> parent, K key) {
            this.parent = parent;
            this.key = key;
        }

        public MapBuilder value(V value) {
            parent.addEntry(key, value);
            return parent;
        }
    }

}