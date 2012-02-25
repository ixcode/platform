package ixcode.platform.http.protocol.request;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static ixcode.platform.http.protocol.request.RequestParameters.requestParametersFrom;
import static ixcode.platform.io.IoStreamHandling.readFully;

public class Request {
    private transient HttpServletRequest httpServletRequest;

    public final RequestParameters parameters;
    public final String subpath;
    public final String body;

    public static Request requestFrom(HttpServletRequest httpServletRequest) {
        return new Request(requestParametersFrom(httpServletRequest), bodyFrom(httpServletRequest), httpServletRequest, "");
    }

    public static Request requestWithUriParameters(Request request, RequestParameters includingUri, String subpath) {
        return new Request(includingUri, request.body, request.httpServletRequest, subpath);
    }

    private Request(RequestParameters parameters, String body, HttpServletRequest httpServletRequest, String subpath) {
        this.parameters = parameters;
        this.body = body;
        this.httpServletRequest = httpServletRequest;
        this.subpath = subpath;
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