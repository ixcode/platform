package ixcode.platform.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public final class LinkedHashMapRepository<T> implements Repository<T> {

    private final Map<RepositoryKey, RepositoryItem<T>> storage = new LinkedHashMap<RepositoryKey, RepositoryItem<T>>();
    private final RepositoryKeyGenerator keyGenerator;
    private Class<T> itemType;

    public LinkedHashMapRepository(RepositoryKeyGenerator keyGenerator, Class<T> itemType) {
        this.keyGenerator = keyGenerator;
        this.itemType = itemType;
    }

    @Override
    public RepositoryKey put(T object) {
        RepositoryKey key = keyGenerator.generateKeyFor(this.getClass(), object);

        storage.put(key, new RepositoryItem(key, object));

        return key;
    }

    @Override
    public List<RepositoryKey> put(T... objects) {
        List<RepositoryKey> keys = new ArrayList<RepositoryKey>();
        for (T obj : objects) {
            keys.add(put(obj));
        }
        return keys;
    }

    @Override
    public T get(RepositoryKey key) {
        return storage.get(key).value;
    }

    @Override
    public List<RepositoryItem> find(RepositorySearch search) {
        return new ArrayList<RepositoryItem>(storage.values());
    }

    @Override
    public Class<T> getItemType() {
        return itemType;
    }
}