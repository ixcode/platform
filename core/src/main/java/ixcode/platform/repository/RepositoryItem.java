package ixcode.platform.repository;

public class RepositoryItem<T> {
    
    public final RepositoryKey repositoryKey;
    public final T value;

    public RepositoryItem(RepositoryKey repositoryKey, T value) {
        this.repositoryKey = repositoryKey;
        this.value = value;
    }
}