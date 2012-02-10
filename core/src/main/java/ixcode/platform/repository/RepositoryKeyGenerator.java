package ixcode.platform.repository;

public interface RepositoryKeyGenerator {
    RepositoryKey generateKeyFor(String forRepository, Object instance);
}