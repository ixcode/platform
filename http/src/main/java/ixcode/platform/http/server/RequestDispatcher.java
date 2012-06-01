package ixcode.platform.http.server;


import ixcode.platform.http.protocol.ContentType;
import ixcode.platform.http.protocol.IanaContentType;
import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.protocol.response.ResponseBuilder;
import ixcode.platform.http.server.resource.ResourceHyperlinkBuilder;
import ixcode.platform.http.server.resource.ResourceInvocation;
import ixcode.platform.http.server.resource.ResourceLookup;
import ixcode.platform.http.server.resource.ResourceNotFoundException;
import ixcode.platform.http.server.resource.RouteMap;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ixcode.platform.http.protocol.IanaContentType.json;
import static ixcode.platform.http.protocol.IanaContentType.xml;
import static ixcode.platform.http.protocol.request.Request.requestFrom;
import static ixcode.platform.http.protocol.response.ResponseLinkBuilder.linkBuilderFrom;
import static java.lang.Thread.currentThread;

public class RequestDispatcher extends HttpServlet {

    private static final Logger log = Logger.getLogger(RequestDispatcher.class);

    private final String uriRoot;
    private final ResourceLookup resourceLookup;
    private final ResourceHyperlinkBuilder resourceHyperlinkBuilder;
    private final ContentType defaultContentType;

    public static RequestDispatcher requestDispatcher(String uriRoot,
                                                      RouteMap routeMap,
                                                      ContentType defaultContentType) {
        return new RequestDispatcher(uriRoot, routeMap, routeMap, defaultContentType);
    }

    public RequestDispatcher(String uriRoot, 
                             ResourceLookup resourceLookup, 
                             ResourceHyperlinkBuilder resourceHyperlinkBuilder,
                             ContentType defaultContentType) {

        this.uriRoot = uriRoot;
        this.resourceLookup = resourceLookup;
        this.resourceHyperlinkBuilder = resourceHyperlinkBuilder;
        this.defaultContentType = defaultContentType;
        log.info("Default content type: " + defaultContentType);

    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        Request request = requestFrom(httpServletRequest);
        currentThread().setName(currentThread().getName() + "/" + request.getPath());

        ContentType contentType = determineContentTypeForResponse(request);

        ResponseBuilder responseBuilder = new ResponseBuilder(linkBuilderFrom(httpServletRequest), contentType);

        try {
            ResourceInvocation invoke = resourceLookup.resourceMappedTo(request);
            invoke.GET(request, responseBuilder, resourceHyperlinkBuilder);
        } catch (ResourceNotFoundException e) {
            if (request.getPath().equals("/favicon.ico")) {
                respondWithDefaultFavicon(responseBuilder);
            } else {
                respondWithResourceNotFound(responseBuilder, e);
            }
        }

        responseBuilder.translateTo(httpServletResponse);
    }

    /**
     * here is where we would do content negotiation
     */
    private ContentType determineContentTypeForResponse(Request request) {
        if (request.getPath().endsWith(".json")) {
            return json;
        } else if (request.getPath().endsWith(".xml")) {
            return xml;
        }
        return defaultContentType;
    }

    private void respondWithDefaultFavicon(ResponseBuilder responseBuilder) {
        responseBuilder.status().ok()
                       .contentType().png()
                       .bodyFromClasspath("icon/platform.png");

    }

    private void respondWithResourceNotFound(ResponseBuilder responseBuilder, ResourceNotFoundException e) {
        log.info(e.getMessage());
        responseBuilder
                .status().notFound()
                .contentType().json()
                .body(String.format("{ \"is\" : \"problem\",  \"code\" : \"%s\", \"HTTP/1.1 Status\" : \"404\", \"description\" : \"Resource not found\", \"path\" : \"%s\"}", e.getSystemErrorCode(), e.getPath()));
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        Request request = requestFrom(httpServletRequest);
        ResponseBuilder responseBuilder = new ResponseBuilder(linkBuilderFrom(httpServletRequest), json);

        try {
            ResourceInvocation invoke = resourceLookup.resourceMappedTo(request);
            invoke.POST(request, responseBuilder, resourceHyperlinkBuilder);
        } catch (ResourceNotFoundException e) {
            respondWithResourceNotFound(responseBuilder, e);
        }

        responseBuilder.translateTo(httpServletResponse);
    }


}