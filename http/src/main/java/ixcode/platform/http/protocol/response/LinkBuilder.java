package ixcode.platform.http.protocol.response;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

import static ixcode.platform.http.protocol.UriFactory.uri;
import static java.lang.String.format;

public class LinkBuilder {

    private final String template;

    public static LinkBuilder linkBuilderFrom(HttpServletRequest httpServletRequest) {
        return new LinkBuilder(httpServletRequest.getServerName(),
                httpServletRequest.getLocalPort(),
                httpServletRequest.getContextPath());
    }

    public LinkBuilder(String host, int port, String contextPath) {
        this.template = format("http://%s:%d", host, port) + "%s";
    }

    public URI linkTo(String path) {
        return uri(format(template, path));
    }

}