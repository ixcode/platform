package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.HttpMethod;
import ixcode.platform.http.server.resource.path.PathMatch;
import ixcode.platform.http.server.resource.path.PathPattern;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

class ResourceMapping {
    public final Resource resource;
    private PathPattern pathPattern;
    public Set<HttpMethod> allowedHttpMethods;

    ResourceMapping(Resource resource, HttpMethod[] httpMethods, PathPattern pathPattern) {
        this.resource = resource;
        this.pathPattern = pathPattern;
        this.allowedHttpMethods = new HashSet<HttpMethod>(asList(httpMethods));
    }

    public boolean allowsHttpMethod(String httpMethod) {
        return allowedHttpMethods.contains(HttpMethod.valueOf(httpMethod.toUpperCase()));
    }


    public PathMatch match(String path) {
        return pathPattern.match(path);
    }
}