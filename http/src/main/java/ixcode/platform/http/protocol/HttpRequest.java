package ixcode.platform.http.protocol;

import javax.servlet.http.*;

public class HttpRequest {

    public static HttpRequest httpRequestFrom(HttpServletRequest httpServletRequest) {
        return new HttpRequest();
    }
}