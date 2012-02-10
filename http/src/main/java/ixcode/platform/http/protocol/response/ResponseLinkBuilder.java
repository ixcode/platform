package ixcode.platform.http.protocol.response;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

import static ixcode.platform.http.protocol.UriFactory.uri;
import static java.lang.String.format;

public class ResponseLinkBuilder {

    private final String template;

    public static ResponseLinkBuilder linkBuilderFrom(HttpServletRequest httpServletRequest) {
        return new ResponseLinkBuilder(httpServletRequest.getServerName(),
                httpServletRequest.getLocalPort(),
                httpServletRequest.getContextPath());
    }

    public ResponseLinkBuilder(String host, int port, String contextPath) {
        this.template = format("http://%s:%d", host, port) + "%s";
    }

    public URI linkTo(String path) {
        return uri(format(template, path));
    }

}