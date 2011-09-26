package ixcode.platform.http;


import ixcode.platform.http.protocol.*;
import ixcode.platform.http.server.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import static ixcode.platform.http.protocol.HttpRequest.*;

public class RequestDispatcher extends HttpServlet {

    private final ResourceLookup resourceLookup;

    public RequestDispatcher(ResourceLookup resourceLookup) {
        this.resourceLookup = resourceLookup;
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        Resource resource = resourceLookup.findTheResourceMappedToThe(httpServletRequest);

        HttpRequest httpRequest = httpRequestFrom(httpServletRequest);
        HttpResponse httpResponse = new HttpResponse();

        resource.GET(httpRequest, httpResponse);

        httpResponse.translateTo(httpServletResponse);
    }


}