package ixcode.platform.http.server.resource;

import ixcode.platform.di.InjectionContext;
import ixcode.platform.http.protocol.HttpMethod;
import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.server.resource.path.UriTemplate;
import ixcode.platform.http.server.resource.path.UriTemplateMatch;
import ixcode.platform.reflect.ObjectFactory;

import java.util.ArrayList;
import java.util.List;

import static ixcode.platform.http.protocol.HttpMethod.GET;
import static ixcode.platform.http.protocol.HttpMethod.POST;
import static ixcode.platform.http.server.resource.path.UriTemplate.uriTemplateFrom;
import static java.lang.String.format;
import static java.util.Collections.sort;

public class RouteMap implements ResourceLookup, ResourceHyperlinkBuilder {

    private final List<ResourceMapping> resourceMappings;
    private final String uriRoot;
    private InjectionContext injectionContext;

    public static RouteMap aResourceMap() {
        return new RouteMap();
    }

    public static RouteMap aResourceMapRootedAt(String uriRoot) {
        return new RouteMap(uriRoot);
    }

    private RouteMap() {
        this(null);
    }

    private RouteMap(String uriRoot) {
        this.uriRoot = uriRoot;
        this.resourceMappings = new ArrayList<ResourceMapping>();
    }

    private RouteMap(String uriRoot,
                     List<ResourceMapping> resourceMappings,
                     InjectionContext injectionContext) {
        this.uriRoot = uriRoot;
        this.resourceMappings = resourceMappings;
        this.injectionContext = injectionContext;
    }

    public RouteMap rootedAt(String uriRoot) {
        return new RouteMap(uriRoot, resourceMappings, injectionContext);
    }


    public EntryBuilder thePath(String path) {
        return new EntryBuilder(this, path);
    }

    public ResourceInvocation resourceMappedTo(Request request) {
        String path = removeTrailingSlashIfNotRoot(request.getPath());
        path = removeFileTypesIfKnown(path);

        List<MatchRanking> rankings = new ArrayList<MatchRanking>();
        for (ResourceMapping resourceMapping : resourceMappings) {
            UriTemplateMatch match = resourceMapping.match(path);
            if (match.level > 0) {
                rankings.add(new MatchRanking(match, resourceMapping));
            }
        }


        if (rankings.isEmpty()) {
            throw new ResourceNotFoundException(path);
        }

        sort(rankings);

        MatchRanking bestMatchRanking = rankings.get(0);

        checkForDuplicateMatch(path, rankings, bestMatchRanking);

        ResourceMapping matched = bestMatchRanking.resourceMapping;
        UriTemplateMatch uriTemplateMatch = bestMatchRanking.uriTemplateMatch;


        if (matched.allowsHttpMethod(request.getMethod())) {
            return new ResourceInvocation(matched.resource, uriTemplateMatch);
        }

        throw new RuntimeException(format("Resource [%s] does not support the [%s] method", matched.resource, request.getMethod()));
    }

    private String removeFileTypesIfKnown(String path) {
        if (path.endsWith(".json")) {
            return path.substring(0, path.length() - 5);
        } else if (path.endsWith(".xml")) {
            return path.substring(0, path.length() - 4);
        }
        return path;
    }

    private void checkForDuplicateMatch(String path, List<MatchRanking> rankings, MatchRanking bestMatchRanking) {
        if (rankings.size() > 1
                && (bestMatchRanking.level == rankings.get(1).level)) {
            throw new MultipleResourceMatchedException(path, bestMatchRanking.resourceMapping.resource, rankings.get(rankings.size() - 2).resourceMapping);
        }
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


    private void registerMapping(String uriRoot, String path, Resource resource, HttpMethod... httpMethods) {
        resourceMappings.add(new ResourceMapping(resource, httpMethods, uriTemplateFrom(uriRoot, path)));
    }

    @Override public <T extends UriTemplateGenerator> T linkTo(Class<T> templateClass) {
        return new ObjectFactory<T>().instantiateWithArg(templateClass, ResourceLookup.class, this);
    }

    public RouteMap withInjectionContext(InjectionContext injectionContext) {
        this.injectionContext = injectionContext;
        return this;
    }

    public RouteMap withTemplatedPages(TemplatedPageMap templatedPages) {
        for (TemplatedPage entry : templatedPages) {
            TemplatedPageResource resource = new TemplatedPageResource(
                    templatedPages.templateEngine, entry);

            registerMapping(this.uriRoot, entry.path, resource, entry.methods);
        }


        return this;
    }


    public static class EntryBuilder {
        private final RouteMap parent;
        private String path;
        private Resource resource;


        private EntryBuilder(RouteMap parent, String path) {
            this.parent = parent;
            this.path = path;
        }

        public EntryBuilder toA(Resource resource) {
            this.resource = resource;
            return this;
        }

        public RouteMap supporting(HttpMethod... httpMethods) {
            parent.registerMapping(parent.uriRoot, path, resource, httpMethods);
            return parent;
        }

        public RouteMap toThe(Class<? extends Resource> resourceClass) {
            parent.registerMapping(parent.uriRoot, path, parent.injectionContext.getA(resourceClass), httpMethodsFrom(resourceClass));
            return parent;
        }

        private HttpMethod[] httpMethodsFrom(Class<? extends Resource> resourceClass) {
            List<HttpMethod> httpMethods = new ArrayList<HttpMethod>();

            if (GetResource.class.isAssignableFrom(resourceClass)) {
                httpMethods.add(GET);
            }
            if (PostResource.class.isAssignableFrom(resourceClass)) {
                httpMethods.add(POST);
            }

            return httpMethods.toArray(new HttpMethod[0]);
        }
    }


    private class MatchRanking implements Comparable<MatchRanking> {
        public final int level;
        private final UriTemplateMatch uriTemplateMatch;
        public final ResourceMapping resourceMapping;

        private final Integer comparableValue;


        private MatchRanking(UriTemplateMatch uriTemplateMatch, ResourceMapping resourceMapping) {
            this.uriTemplateMatch = uriTemplateMatch;
            this.level = uriTemplateMatch.level;
            this.resourceMapping = resourceMapping;
            this.comparableValue = new Integer(this.level);
        }

        @Override public int compareTo(MatchRanking o) {
            return o.comparableValue.compareTo(comparableValue);
        }
    }
}