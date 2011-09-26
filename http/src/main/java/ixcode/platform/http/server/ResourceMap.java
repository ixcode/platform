package ixcode.platform.http.server;

import ixcode.platform.http.protocol.*;

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

    public Resource findTheResourceMappedToThe(HttpServletRequest httpServletRequest) {
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

    private void registerMapping(String path, Resource resource, HttpVerb[] verbs) {
        resources.put(path, new ResourceMapping(resource, verbs));
    }

    public static class EntryBuilder {
        private final ResourceMap parent;
        private String path;
        private Resource resource;


        private EntryBuilder(ResourceMap parent, String path) {
            this.parent = parent;
            this.path = path;
        }

        public EntryBuilder toA(Resource resource) {
            this.resource = resource;
            return this;
        }

        public ResourceMap supporting(HttpVerb... verbs) {
            parent.registerMapping(path, resource, verbs);
            return parent;
        }
    }


    private static class ResourceMapping {
        public final Resource resource;
        public Set<HttpVerb> allowedVerbs;

        private ResourceMapping(Resource resource, HttpVerb[] verbs) {
            this.resource = resource;
            this.allowedVerbs = new HashSet<HttpVerb>(asList(verbs));
        }

        public boolean allowsVerb(String method) {
            return allowedVerbs.contains(HttpVerb.valueOf(method.toUpperCase()));
        }


    }
}