package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.server.resource.path.UriTemplate;

import javax.servlet.http.*;
import java.util.List;

public interface ResourceLookup {
    ResourceInvocation resourceMappedTo(Request request);

    List<UriTemplate> uriTemplateMappedTo(Class<?> resourceClass);
}