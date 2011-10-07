package ixcode.platform.http.server;


import ixcode.platform.http.protocol.*;
import ixcode.platform.http.resource.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import static ixcode.platform.http.protocol.Request.*;

public class RequestDispatcher extends HttpServlet {

    private final ResourceLookup resourceLookup;

    public RequestDispatcher(ResourceLookup resourceLookup) {
        this.resourceLookup = resourceLookup;
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        HttpResource resource = resourceLookup.findTheResourceMappedToThe(httpServletRequest);

        Request request = httpRequestFrom(httpServletRequest);
        ResponseBuilder responseBuilder = new ResponseBuilder();

        resource.GET(request, responseBuilder);

        responseBuilder.translateTo(httpServletResponse);
    }


}