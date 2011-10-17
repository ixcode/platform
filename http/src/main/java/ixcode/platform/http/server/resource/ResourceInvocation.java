package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.protocol.response.ResponseBuilder;
import ixcode.platform.http.server.resource.path.PathMatch;

public class ResourceInvocation {
    private final Resource resource;
    private final PathMatch pathMatch;

    public ResourceInvocation(Resource resource, PathMatch pathMatch) {
        this.resource = resource;
        this.pathMatch = pathMatch;
    }

    public void GET(Request request, ResponseBuilder responseBuilder) {
        resource.GET(request, responseBuilder, null);
    }
}