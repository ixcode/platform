package ixcode.platform.repository;

import java.util.List;

public interface Repository<T> {

    RepositoryKey put(T object);

    List<RepositoryKey> put(T... object);

    T get(RepositoryKey key);

    List<RepositoryItem> find(RepositorySearch search);

    Class<T> getItemType();

    String getRepositoryId();
}