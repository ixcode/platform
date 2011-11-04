package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.HttpMethod;
import ixcode.platform.http.server.resource.path.UriTemplateMatch;
import ixcode.platform.http.server.resource.path.UriTemplate;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

class ResourceMapping {
    public final Resource resource;
    public UriTemplate uriTemplate;
    public Set<HttpMethod> allowedHttpMethods;

    ResourceMapping(Resource resource, HttpMethod[] httpMethods, UriTemplate uriTemplate) {
        this.resource = resource;
        this.uriTemplate = uriTemplate;
        this.allowedHttpMethods = new HashSet<HttpMethod>(asList(httpMethods));
    }

    public boolean allowsHttpMethod(String httpMethod) {
        return allowedHttpMethods.contains(HttpMethod.valueOf(httpMethod.toUpperCase()));
    }


    public UriTemplateMatch match(String path) {
        return uriTemplate.match(path);
    }


    public boolean isTo(Class<?> resourceClass) {
        return resource.getClass().equals(resourceClass);
    }
}