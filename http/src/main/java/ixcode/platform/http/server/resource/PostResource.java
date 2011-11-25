package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.protocol.response.ResponseBuilder;

public interface PostResource extends Resource {


    void POST(Request request, ResponseBuilder respondWith, ResourceHyperlinkBuilder hyperlinkBuilder);
}