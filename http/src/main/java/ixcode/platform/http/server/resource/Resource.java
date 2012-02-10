package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.protocol.response.LinkBuilder;
import ixcode.platform.http.protocol.response.ResponseBuilder;

public interface Resource {

   // @todo add RequestParameters as a parameter here
    // @todo and move this down to GetResource
    void GET(Request request, ResponseBuilder respondWith, ResourceHyperlinkBuilder hyperlinkBuilder);


}