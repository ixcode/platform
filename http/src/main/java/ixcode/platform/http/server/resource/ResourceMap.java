package ixcode.platform.http.server.resource;

import ixcode.platform.di.InjectionContext;
import ixcode.platform.http.protocol.HttpMethod;
import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.server.resource.path.UriTemplate;
import ixcode.platform.http.server.resource.path.UriTemplateMatch;
import ixcode.platform.reflect.ObjectFactory;

import java.util.ArrayList;
import java.util.List;

import static ixcode.platform.http.server.resource.path.UriTemplate.uriTemplateFrom;
import static java.lang.String.format;

public class ResourceMap implements ResourceLookup, ResourceHyperlinkBuilder {

    private final List<ResourceMapping> resourceMappings = new ArrayList<ResourceMapping>();
    private final String uriRoot;
    private InjectionContext injectionContext;


    public static ResourceMap aResourceMapRootedAt(String uriRoot) {
        return new ResourceMap(uriRoot);
    }

    private ResourceMap(String uriRoot) {
        this.uriRoot = uriRoot;
    }


    public EntryBuilder mapping(String path) {
        return new EntryBuilder(this, path);
    }

    public ResourceInvocation resourceMappedTo(Request request) {
        String path = removeTrailingSlashIfNotRoot(request.getPath());

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

        throw new RuntimeException(format("Resource [%s] does not support the [%s] method", matched.resource, request.getMethod()));
    }

    private String removeTrailingSlashIfNotRoot(String path) {
        return (path.length() > 1 && path.endsWith("/")) ? path.substring(0, path.length() - 1) : path;
    }

    @Override public List<UriTemplate> uriTemplateMappedTo(Class<?> resourceClass) {
        List<UriTemplate> matchingTemplates = new ArrayList<UriTemplate>();
        for (ResourceMapping resourceMapping : resourceMappings) {
            if (resourceMapping.isTo(resourceClass)) {
                matchingTemplates.add(resourceMapping.uriTemplate);
            }
        }
        if (matchingTemplates.size() == 0) {
            throw new RuntimeException("Could not find a uriTemplate for resource " + resourceClass.getName());
        }
        return matchingTemplates;
    }


    private void registerMapping(String uriRoot, String path, Resource resource, HttpMethod[] httpMethods) {
        resourceMappings.add(new ResourceMapping(resource, httpMethods, uriTemplateFrom(uriRoot, path)));
    }

    @Override public <T extends UriTemplateGenerator> T linkTo(Class<T> templateClass) {
        return new ObjectFactory<T>().instantiateWithArg(templateClass, ResourceLookup.class, this);
    }

    public ResourceMap withInjectionContext(InjectionContext injecttionContext) {
        this.injectionContext = injecttionContext;
        return this;
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
            parent.registerMapping(parent.uriRoot, path, resource, httpMethods);
            return parent;
        }

        public EntryBuilder toA(Class<? extends Resource> resourceClass) {
            toA((Resource) parent.injectionContext.getA(resourceClass));
            return this;
        }
    }


}