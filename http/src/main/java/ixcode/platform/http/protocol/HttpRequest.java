package ixcode.platform.http.protocol;

import javax.servlet.http.*;

import static ixcode.platform.http.protocol.RequestParameters.requestParametersFrom;

public class HttpRequest {
    public final RequestParameters parameters;

    public HttpRequest(RequestParameters parameters) {
        this.parameters = parameters;
    }

    public static HttpRequest httpRequestFrom(HttpServletRequest httpServletRequest) {
        return new HttpRequest(requestParametersFrom(httpServletRequest));
    }

}