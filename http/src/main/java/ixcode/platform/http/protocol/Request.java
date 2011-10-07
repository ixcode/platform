package ixcode.platform.http.protocol;

import javax.servlet.http.*;

import static ixcode.platform.http.protocol.RequestParameters.requestParametersFrom;

public class Request {
    public final RequestParameters parameters;

    public Request(RequestParameters parameters) {
        this.parameters = parameters;
    }

    public static Request httpRequestFrom(HttpServletRequest httpServletRequest) {
        return new Request(requestParametersFrom(httpServletRequest));
    }

}