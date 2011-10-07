package ixcode.platform.http.resource;

import ixcode.platform.http.protocol.*;

public interface HttpResource {
    public void GET(HttpRequest httpRequest, HttpResponseBuilder httpResponseBuilder);
}