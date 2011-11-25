package ixcode.platform.http.protocol.request;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static ixcode.platform.http.protocol.request.RequestParameters.requestParametersFrom;
import static ixcode.platform.io.StreamHandling.readFully;

public class Request {
    public final RequestParameters parameters;
    private transient HttpServletRequest httpServletRequest;
    public final String body;

    public static Request requestFrom(HttpServletRequest httpServletRequest) {
        return new Request(requestParametersFrom(httpServletRequest), bodyFrom(httpServletRequest), httpServletRequest);
    }

    public static Request requestWithUriParameters(Request request, RequestParameters includingUri) {
        return new Request(includingUri, request.body, request.httpServletRequest);
    }

    private Request(RequestParameters parameters, String body, HttpServletRequest httpServletRequest) {
        this.parameters = parameters;
        this.body = body;
        this.httpServletRequest = httpServletRequest;
    }

    private static String bodyFrom(HttpServletRequest httpServletRequest) {
        try {
            return readFully(httpServletRequest.getInputStream(), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Could not read input stream from request (See cause)", e);
        }
    }

    public String getPath() {
        return httpServletRequest.getPathInfo();
    }

    public String getMethod() {
        return httpServletRequest.getMethod();
    }


}