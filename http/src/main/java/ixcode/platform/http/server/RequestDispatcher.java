package ixcode.platform.http.server;


import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.protocol.response.ResponseBuilder;
import ixcode.platform.http.server.resource.ResourceInvocation;
import ixcode.platform.http.server.resource.ResourceLookup;
import ixcode.platform.http.server.resource.ResourceNotFoundException;
import ixcode.platform.json.JsonObject;
import ixcode.platform.json.printer.JsonPrinter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ixcode.platform.http.protocol.request.Request.httpRequestFrom;

public class RequestDispatcher extends HttpServlet {

    private final ResourceLookup resourceLookup;

    public RequestDispatcher(ResourceLookup resourceLookup) {
        this.resourceLookup = resourceLookup;
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        Request request = httpRequestFrom(httpServletRequest);
        ResponseBuilder responseBuilder = new ResponseBuilder();

        try {
            ResourceInvocation resource = resourceLookup.resourceMappedTo(request);
            resource.GET(request, responseBuilder);
        } catch (ResourceNotFoundException e) {
            responseBuilder
                    .status().notFound()
                    .contentType().json()
                    .body(String.format("{ \"is\" : \"problem\",  \"code\" : \"%s\", \"HTTP/1.1 Status\" : \"404\", \"description\" : \"Resource not found\", \"path\" : \"%s\"}", e.getSystemErrorCode(), e.getPath()));
        }

        responseBuilder.translateTo(httpServletResponse);
    }


}