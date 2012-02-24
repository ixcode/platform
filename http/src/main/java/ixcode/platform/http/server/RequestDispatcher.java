package ixcode.platform.http.server;


import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.protocol.response.ResponseBuilder;
import ixcode.platform.http.server.resource.*;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ixcode.platform.http.protocol.request.Request.requestFrom;
import static ixcode.platform.http.protocol.response.ResponseLinkBuilder.linkBuilderFrom;

public class RequestDispatcher extends HttpServlet {

    private static final Logger log = Logger.getLogger(RequestDispatcher.class);

    private final String uriRoot;
    private final ResourceLookup resourceLookup;
    private final ResourceHyperlinkBuilder resourceHyperlinkBuilder;

    public static RequestDispatcher requestDispatcher(String uriRoot, RouteMap routeMap) {
        return new RequestDispatcher(uriRoot, routeMap, routeMap);
    }

    public RequestDispatcher(String uriRoot, ResourceLookup resourceLookup, ResourceHyperlinkBuilder resourceHyperlinkBuilder) {
        this.uriRoot = uriRoot;
        this.resourceLookup = resourceLookup;
        this.resourceHyperlinkBuilder = resourceHyperlinkBuilder;
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        Request request = requestFrom(httpServletRequest);
        ResponseBuilder responseBuilder = new ResponseBuilder(linkBuilderFrom(httpServletRequest));

        try {
            ResourceInvocation invoke = resourceLookup.resourceMappedTo(request);
            invoke.GET(request, responseBuilder, resourceHyperlinkBuilder);
        } catch (ResourceNotFoundException e) {
            log.info(e.getMessage());
            responseBuilder
                    .status().notFound()
                    .contentType().json()
                    .body(String.format("{ \"is\" : \"problem\",  \"code\" : \"%s\", \"HTTP/1.1 Status\" : \"404\", \"description\" : \"Resource not found\", \"path\" : \"%s\"}", e.getSystemErrorCode(), e.getPath()));
        }

        responseBuilder.translateTo(httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        Request request = requestFrom(httpServletRequest);
        ResponseBuilder responseBuilder = new ResponseBuilder(linkBuilderFrom(httpServletRequest));

        try {
            ResourceInvocation invoke = resourceLookup.resourceMappedTo(request);
            invoke.POST(request, responseBuilder, resourceHyperlinkBuilder);
        } catch (ResourceNotFoundException e) {
            log.info(e.getMessage());
            responseBuilder
                    .status().notFound()
                    .contentType().json()
                    .body(String.format("{ \"is\" : \"problem\",  \"code\" : \"%s\", \"HTTP/1.1 Status\" : \"404\", \"description\" : \"Resource not found\", \"path\" : \"%s\"}", e.getSystemErrorCode(), e.getPath()));
        }

        responseBuilder.translateTo(httpServletResponse);
    }


}