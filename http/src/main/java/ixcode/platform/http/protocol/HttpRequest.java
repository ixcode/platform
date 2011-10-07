package ixcode.platform.http.protocol;

import javax.servlet.http.*;

public class HttpRequest {

    private RequestParameters requestParameters;

    public static HttpRequest httpRequestFrom(HttpServletRequest httpServletRequest) {
        return new HttpRequest();
    }

    public RequestParameters getRequestParameters() {
        return requestParameters;
    }
}