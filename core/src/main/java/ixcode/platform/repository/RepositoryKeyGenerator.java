package ixcode.platform.repository;

public interface RepositoryKeyGenerator {
    RepositoryKey generateKeyFor(Class<?> forRepository, Object instance);
}