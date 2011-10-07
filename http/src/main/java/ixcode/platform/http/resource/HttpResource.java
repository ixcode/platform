package ixcode.platform.http.resource;

import ixcode.platform.http.protocol.*;

public interface HttpResource {
    public void GET(Request request, ResponseBuilder responseBuilder);
}