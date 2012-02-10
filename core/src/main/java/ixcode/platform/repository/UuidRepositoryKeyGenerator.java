package ixcode.platform.repository;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public class UuidRepositoryKeyGenerator implements RepositoryKeyGenerator {
    @Override
    public RepositoryKey generateKeyFor(Class<?> forRepository, Object instance) {
        return new RepositoryKey(forRepository, randomUUID().toString());
    }
}