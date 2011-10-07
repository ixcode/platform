package ixcode.platform.http.server;

import ixcode.platform.http.protocol.*;
import ixcode.platform.http.resource.*;

import javax.servlet.http.*;
import java.util.*;

import static java.lang.String.*;
import static java.util.Arrays.asList;

public class ResourceMap implements ResourceLookup {

    private final Map<String, ResourceMapping> resources = new HashMap<String, ResourceMapping>();

    public static ResourceMap aResourceMap() {
        return new ResourceMap();
    }

    public EntryBuilder mapping(String path) {
        return new EntryBuilder(this, path);
    }

    public HttpResource findTheResourceMappedToThe(HttpServletRequest httpServletRequest) {
        String path = httpServletRequest.getPathInfo();
        if (!resources.containsKey(path)) {
            throw new RuntimeException(format("Could not find a resource mapped to [%s]", path));
        }

        ResourceMapping resourceMapping = resources.get(path);
        if (resourceMapping.allowsVerb(httpServletRequest.getMethod())) {
            return resourceMapping.resource;
        }

        throw new RuntimeException(format("Resource does not support the [%s] method", httpServletRequest.getMethod()));
    }

    private void registerMapping(String path, HttpResource resource, Method[] methods) {
        resources.put(path, new ResourceMapping(resource, methods));
    }

    public static class EntryBuilder {
        private final ResourceMap parent;
        private String path;
        private HttpResource resource;


        private EntryBuilder(ResourceMap parent, String path) {
            this.parent = parent;
            this.path = path;
        }

        public EntryBuilder toA(HttpResource resource) {
            this.resource = resource;
            return this;
        }

        public ResourceMap supporting(Method... methods) {
            parent.registerMapping(path, resource, methods);
            return parent;
        }
    }


    private static class ResourceMapping {
        public final HttpResource resource;
        public Set<Method> allowedMethods;

        private ResourceMapping(HttpResource resource, Method[] methods) {
            this.resource = resource;
            this.allowedMethods = new HashSet<Method>(asList(methods));
        }

        public boolean allowsVerb(String method) {
            return allowedMethods.contains(Method.valueOf(method.toUpperCase()));
        }


    }
}