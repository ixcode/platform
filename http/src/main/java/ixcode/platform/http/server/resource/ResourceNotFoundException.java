package ixcode.platform.http.server.resource;

import ixcode.platform.http.server.HasSystemErrorCode;

import static java.lang.String.format;

public class ResourceNotFoundException extends RuntimeException implements HasSystemErrorCode {
    private final String path;

    public ResourceNotFoundException(String path) {
        super(format("Could not find a resource mapped to [%s]", path));
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override public String getSystemErrorCode() {
        return "SYS-001";
    }
}