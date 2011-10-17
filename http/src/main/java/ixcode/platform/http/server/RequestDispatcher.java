package ixcode.platform.http.server;


import ixcode.platform.http.protocol.request.*;
import ixcode.platform.http.protocol.response.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import static ixcode.platform.http.protocol.request.Request.*;

public class RequestDispatcher extends HttpServlet {

    private final ResourceLookup resourceLookup;

    public RequestDispatcher(ResourceLookup resourceLookup) {
        this.resourceLookup = resourceLookup;
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        Resource resource = resourceLookup.findTheResourceMappedToThe(httpServletRequest);

        Request request = httpRequestFrom(httpServletRequest);
        ResponseBuilder responseBuilder = new ResponseBuilder();

        resource.GET(request, responseBuilder);

        responseBuilder.translateTo(httpServletResponse);
    }


}