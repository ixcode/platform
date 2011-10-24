package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.protocol.response.ResponseBuilder;
import ixcode.platform.http.server.resource.path.UriTemplateMatch;

public class ResourceInvocation {
    private final Resource resource;
    private final UriTemplateMatch uriTemplateMatch;

    public ResourceInvocation(Resource resource, UriTemplateMatch uriTemplateMatch) {
        this.resource = resource;
        this.uriTemplateMatch = uriTemplateMatch;
    }

    public void GET(Request request, ResponseBuilder responseBuilder) {
        resource.GET(request, responseBuilder, null);
    }
}