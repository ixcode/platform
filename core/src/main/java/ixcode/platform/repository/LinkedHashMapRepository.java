package ixcode.platform.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Arrays.asList;

public final class LinkedHashMapRepository<T> implements Repository<T> {

    private final Map<RepositoryKey, RepositoryItem<T>> storage = new LinkedHashMap<RepositoryKey, RepositoryItem<T>>();
    private String repositoryId;
    private final RepositoryKeyGenerator keyGenerator;
    private Class<T> itemType;

    public LinkedHashMapRepository(String repositoryId, RepositoryKeyGenerator keyGenerator, Class<T> itemType) {
        this.repositoryId = repositoryId;
        this.keyGenerator = keyGenerator;
        this.itemType = itemType;
    }

    public static <T> Repository<T> createRepositoryFor(Class<T> classOfItems) {
        if (!classOfItems.isAnnotationPresent(RepositoryId.class)) {
            throw new RuntimeException("Could not find the annotation 'RepositoryId' on your item class [" + classOfItems.getName() + "]");
        }
        String repositoryId = classOfItems.getAnnotation(RepositoryId.class).value();
        return new LinkedHashMapRepository<T>(
                repositoryId,
                new UuidRepositoryKeyGenerator(), classOfItems);
    }

    @Override
    public RepositoryKey put(T object) {
        RepositoryKey key = keyGenerator.generateKeyFor(repositoryId, object);

        put(key, object);

        return key;
    }

    @Override
    public void put(RepositoryKey key, T object) {
        storage.put(key, new RepositoryItem(key, object));
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
        if (!storage.containsKey(key)) {
            throw new RuntimeException(format("Object with key [%s] not found in repository [%s]", key, repositoryId));
        }
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

    @Override
    public String getRepositoryId() {
        return repositoryId;
    }
}