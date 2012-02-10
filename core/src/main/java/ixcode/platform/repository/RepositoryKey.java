package ixcode.platform.repository;

public class RepositoryKey {

    private final Class<?> forRepository;
    private Object key;

    public RepositoryKey(Class<?> forRepository, Object key) {
        this.forRepository = forRepository;
        this.key = key;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepositoryKey that = (RepositoryKey) o;

        if (!key.equals(that.key)) return false;
        if (!forRepository.isAssignableFrom(that.forRepository)) return false;

        return true;
    }

    @Override
    public final int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return key.toString();
    }
}