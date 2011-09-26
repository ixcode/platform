package ixcode.platform.http.server;

import ixcode.platform.http.protocol.*;

public interface Resource {
    public void GET(HttpRequest httpRequest, HttpResponse httpResponse);
}