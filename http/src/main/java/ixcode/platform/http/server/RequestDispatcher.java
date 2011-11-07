package ixcode.platform.http.server;


import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.protocol.response.ResponseBuilder;
import ixcode.platform.http.server.resource.ResourceHyperlinkBuilder;
import ixcode.platform.http.server.resource.ResourceInvocation;
import ixcode.platform.http.server.resource.ResourceLookup;
import ixcode.platform.http.server.resource.ResourceMap;
import ixcode.platform.http.server.resource.ResourceNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ixcode.platform.http.protocol.request.Request.requestFrom;

public class RequestDispatcher extends HttpServlet {

    private final ResourceLookup resourceLookup;
    private final ResourceHyperlinkBuilder resourceHyperlinkBuilder;

    public static RequestDispatcher requestDispatcher(ResourceMap resourceMap) {
        return new RequestDispatcher(resourceMap, resourceMap);
    }

    public RequestDispatcher(ResourceLookup resourceLookup, ResourceHyperlinkBuilder resourceHyperlinkBuilder) {
        this.resourceLookup = resourceLookup;
        this.resourceHyperlinkBuilder = resourceHyperlinkBuilder;
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        Request request = requestFrom(httpServletRequest);
        ResponseBuilder responseBuilder = new ResponseBuilder();

        try {
            ResourceInvocation invoke = resourceLookup.resourceMappedTo(request);
            invoke.GET(request, responseBuilder, resourceHyperlinkBuilder);
        } catch (ResourceNotFoundException e) {
            responseBuilder
                    .status().notFound()
                    .contentType().json()
                    .body(String.format("{ \"is\" : \"problem\",  \"code\" : \"%s\", \"HTTP/1.1 Status\" : \"404\", \"description\" : \"Resource not found\", \"path\" : \"%s\"}", e.getSystemErrorCode(), e.getPath()));
        }

        responseBuilder.translateTo(httpServletResponse);
    }


}