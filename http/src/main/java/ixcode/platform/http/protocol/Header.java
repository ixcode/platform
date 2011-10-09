package ixcode.platform.http.protocol;

import ixcode.platform.text.*;

import javax.servlet.http.*;
import java.net.*;

public class Header {
    private static final UriFormat uriFormat = new UriFormat();

    private final String name;
    private final String value;

    public static Header httpHeader(String name, String value) {
        return new Header(name, value);
    }

    public static Header locationHeader(URI location) {
        return httpHeader("location", uriFormat.format(location));
    }

    private Header(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public void addTo(HttpServletResponse httpServletResponse) {
        httpServletResponse.addHeader(name, value);
    }
}