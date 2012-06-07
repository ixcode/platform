package ixcode.platform.repository;

import java.util.List;
import java.util.Map;

import static ixcode.platform.repository.LinkedHashMapRepository.repositoryIdFor;

public interface Repository<T> {

    RepositoryKey put(T object);

    void put(RepositoryKey key, T object);

    List<RepositoryKey> put(T... object);

    T get(RepositoryKey key);

    List<RepositoryItem> find(RepositorySearch search);

    Class<T> getItemType();

    String getRepositoryId();
}