package ixcode.platform.repository;

public class RepositoryKey {

    public final String repositoryId;
    public final Object key;
    public static final RepositoryKey NEW = new RepositoryKey("NEW", null);

    public RepositoryKey(String repositoryId, Object key) {
        this.repositoryId = repositoryId;
        this.key = key;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepositoryKey that = (RepositoryKey) o;

        if (!key.equals(that.key)) return false;
        if (!repositoryId.equals(that.repositoryId)) return false;

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