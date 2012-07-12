package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.protocol.response.ResponseBuilder;

public class LogoutResource implements GetResource {

    @Override public void GET(Request request, ResponseBuilder respondWith, ResourceHyperlinkBuilder hyperlinkBuilder) {
        request.logout();

        respondWith.status().ok()
                   .contentType().html()
                   .body("<html><h1>You are now logged out</h1></html>");

    }
}