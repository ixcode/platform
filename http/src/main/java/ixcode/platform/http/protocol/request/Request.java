package ixcode.platform.http.protocol.request;

import javax.servlet.http.*;

public class Request {
    public final RequestParameters parameters;

    public Request(RequestParameters parameters) {
        this.parameters = parameters;
    }

    public static Request httpRequestFrom(HttpServletRequest httpServletRequest) {
        return new Request(RequestParameters.requestParametersFrom(httpServletRequest));
    }

}