package ixcode.platform.http.server.resource;

import static java.lang.String.format;

public class MultipleResourceMatchedException extends RuntimeException {
    public MultipleResourceMatchedException(String path, Object resourceA, Object resourceB) {
        super(format("Two resources match the same path [%s] (%s, %s])", path, resourceA.getClass().getSimpleName(), resourceB.getClass().getSimpleName()));
    }
}