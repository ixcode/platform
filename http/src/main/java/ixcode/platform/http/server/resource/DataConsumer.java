package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.request.Request;

public interface DataConsumer {
    void consumeRequest(Request request);
}