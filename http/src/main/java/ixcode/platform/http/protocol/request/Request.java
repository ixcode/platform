package ixcode.platform.http.protocol.request;

import javax.servlet.http.*;

import static ixcode.platform.http.protocol.request.RequestParameters.requestParametersFrom;

public class Request {
    public final RequestParameters parameters;
    private transient HttpServletRequest httpServletRequest;

    public static Request requestFrom(HttpServletRequest httpServletRequest) {
        return new Request(requestParametersFrom(httpServletRequest), httpServletRequest);
    }

    public static Request requestWithUriParameters(Request request, RequestParameters includingUri) {
        return new Request(includingUri, request.httpServletRequest);
    }

    private Request(RequestParameters parameters, HttpServletRequest httpServletRequest) {
        this.parameters = parameters;
        this.httpServletRequest = httpServletRequest;
    }

    public String getPath() {
        return httpServletRequest.getPathInfo();
    }

    public String getMethod() {
        return httpServletRequest.getMethod();
    }


}