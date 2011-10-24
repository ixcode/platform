package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.HttpMethod;
import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.server.resource.path.UriTemplateMatch;

import java.util.ArrayList;
import java.util.List;

import static ixcode.platform.http.server.resource.path.UriTemplate.uriTemplateFrom;
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

        int[] closestMatch = {0, 0};
        ResourceMapping matched = null;
        UriTemplateMatch uriTemplateMatch = null;
        for (ResourceMapping resourceMapping : resourceMappings) {
            UriTemplateMatch match = resourceMapping.match(path);
            if (match.level > 0) {
                if (match.level == closestMatch[0]
                        && match.parameters.size() == closestMatch[1])
                throw new MultipleResourceMatchedException(path, matched.resource, resourceMapping.resource);
            }
            if (match.level > closestMatch[0]) {
                matched = resourceMapping;
                uriTemplateMatch = match;
            }
        }

        if (matched == null) {
            throw new ResourceNotFoundException(path);
        }

        if (matched.allowsHttpMethod(request.getMethod())) {
            return new ResourceInvocation(matched.resource, uriTemplateMatch);
        }

        throw new RuntimeException(format("Resource does not support the [%s] method", request.getMethod()));
    }

    private void registerMapping(String path, Resource resource, HttpMethod[] httpMethods) {
        resourceMappings.add(new ResourceMapping(resource, httpMethods, uriTemplateFrom(path)));
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