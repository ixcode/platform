package ixcode.platform.http.resource;

import java.net.*;
import java.util.*;

import static java.lang.String.*;

public class ResourceRepository<T> {

    private Map<URI, T> resourceMap = new HashMap<URI, T>();

    public URI put(URI uri, T resource) {
        resourceMap.put(uri, resource);
        return uri;
    }

    public Collection<T> resources() {
        return resourceMap.values();
    }

    public T get(URI uri) {
        if (!resourceMap.containsKey(uri)) {
            try {
                throw new RuntimeException(format("404 - Could not find resource at [%s]", uri.toURL().toExternalForm()));
            } catch (MalformedURLException e) {
                throw new RuntimeException("500 - Can't translate URI to URL!: " + uri.toString(), e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("500 - Can't translate URI to URL!: " + uri.toString(), e);
            }
        }
        return resourceMap.get(uri);
    }

    public URI locateResource(final T sourceResource) {
        return locateResource(new ResourceMatcher<T>() {
            public boolean matches(T resource) {
                return sourceResource.equals(resource);
            }

            public String describeFailure() {
                return "an instance that was equal to " + sourceResource;
            }
        });
    }

    public URI locateResource(ResourceMatcher<T> resourceMatcher) {
        for (Map.Entry<URI, T> entry : resourceMap.entrySet()) {
            if (resourceMatcher.matches(entry.getValue())) {
                return entry.getKey();
            }
        }
        throw new RuntimeException("Could not locate resource using matcher: " + resourceMatcher.describeFailure());
    }
}