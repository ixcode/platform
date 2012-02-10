package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.protocol.response.LinkBuilder;
import ixcode.platform.http.protocol.response.ResponseBuilder;
import ixcode.platform.http.server.resource.path.UriTemplateMatch;

import static ixcode.platform.http.protocol.request.Request.requestWithUriParameters;
import static ixcode.platform.http.protocol.request.RequestParameters.appendUriParameters;

public class ResourceInvocation {
    private final Resource resource;
    private final UriTemplateMatch uriTemplateMatch;

    public ResourceInvocation(Resource resource, UriTemplateMatch uriTemplateMatch) {
        this.resource = resource;
        this.uriTemplateMatch = uriTemplateMatch;
    }

    public void GET(Request request, ResponseBuilder responseBuilder, ResourceHyperlinkBuilder resourceMap) {
        Request requestWithUriParameters = requestWithUriParameters(request,
                                                                    appendUriParameters(request.parameters, uriTemplateMatch.parameters),
                                                                    uriTemplateMatch.subpath);

        resource.GET(requestWithUriParameters, responseBuilder, resourceMap);
    }

    public void POST(Request request, ResponseBuilder responseBuilder, ResourceHyperlinkBuilder resourceMap) {
        if (!(resource instanceof PostResource)) {
            throw new RuntimeException("You tried to invoke GET on a resource which is not GETable! " + resource);
        }

        Request requestWithUriParameters = requestWithUriParameters(request,
                                                                    appendUriParameters(request.parameters, uriTemplateMatch.parameters), uriTemplateMatch.subpath);

        ((PostResource)resource).POST(requestWithUriParameters, responseBuilder, resourceMap);
    }
}