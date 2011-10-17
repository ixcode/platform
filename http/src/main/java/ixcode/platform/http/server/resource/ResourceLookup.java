package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.request.Request;

import javax.servlet.http.*;

public interface ResourceLookup {
    ResourceInvocation resourceMappedTo(Request request);
}