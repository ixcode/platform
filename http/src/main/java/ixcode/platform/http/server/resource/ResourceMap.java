package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.HttpMethod;
import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.server.resource.path.PathMatch;

import java.util.ArrayList;
import java.util.List;

import static ixcode.platform.http.server.resource.path.PathPattern.pathPatternFrom;
import static java.lang.String.format;

public class ResourceMap implements ResourceLookup {

    private final List<ResourceMapping> resourceMappings = new ArrayList<ResourceMapping>();

    public static ResourceMap aResourceMap() {
        return new ResourceMap();
    }

    public EntryBuilder mapping(String path) {
        return new EntryBuilder(this, path);
    }

    public ResourceInvocation resourceMappedTo(Request request) {
        String path = request.getPath();

        int closestMatch = 0;
        ResourceMapping matched = null;
        PathMatch pathMatch = null;
        for (ResourceMapping resourceMapping : resourceMappings) {
            PathMatch match = resourceMapping.match(path);
            if (match.level > 0 && match.level == closestMatch) {
                throw new RuntimeException(format("Two resources match the same path [%s] (%s, %s]", path, matched.resource.getClass().getSimpleName(), resourceMapping.resource.getClass().getSimpleName()));
            }
            if (match.level > closestMatch) {
                matched = resourceMapping;
                pathMatch = match;
            }
        }

        if (matched == null) {
            throw new RuntimeException(format("Could not find a resource mapped to [%s]", path));
        }

        if (matched.allowsHttpMethod(request.getMethod())) {
            return new ResourceInvocation(matched.resource, pathMatch);
        }

        throw new RuntimeException(format("Resource does not support the [%s] method", request.getMethod()));
    }

    private void registerMapping(String path, Resource resource, HttpMethod[] httpMethods) {
        resourceMappings.add(new ResourceMapping(resource, httpMethods, pathPatternFrom(path)));
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

        public ResourceMap supporting(HttpMethod... httpMethods) {
            parent.registerMapping(path, resource, httpMethods);
            return parent;
        }
    }


}