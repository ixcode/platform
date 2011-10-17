package ixcode.platform.http.protocol.request;

import javax.servlet.http.*;

import static ixcode.platform.http.protocol.request.RequestParameters.requestParametersFrom;

public class Request {
    public final RequestParameters parameters;
    private HttpServletRequest httpServletRequest;

    public static Request httpRequestFrom(HttpServletRequest httpServletRequest) {
        return new Request(requestParametersFrom(httpServletRequest), httpServletRequest);
    }

    Request(RequestParameters parameters, HttpServletRequest httpServletRequest) {
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