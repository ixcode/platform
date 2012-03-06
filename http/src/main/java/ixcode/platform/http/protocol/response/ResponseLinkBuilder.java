package ixcode.platform.http.protocol.response;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

import static ixcode.platform.http.protocol.UriFactory.uri;
import static java.lang.String.format;

public class ResponseLinkBuilder {

    private final String template;

    public static ResponseLinkBuilder linkBuilderFrom(HttpServletRequest httpServletRequest) {
        String suffix = extractSuffixFromRequest(httpServletRequest);

        return new ResponseLinkBuilder(httpServletRequest.getServerName(),
                                       httpServletRequest.getLocalPort(),
                                       httpServletRequest.getContextPath(),
                                       suffix);
    }

    private static String extractSuffixFromRequest(HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getPathInfo().endsWith(".json")) {
            return ".json";
        } else if (httpServletRequest.getPathInfo().endsWith(".xml")) {
            return ".xml";
        }
        return "";
    }

    public ResponseLinkBuilder(String host, int port, String contextPath, String suffix) {
        this.template = format("http://%s:%d", host, port) + "%s" + suffix;
    }

    public URI linkTo(String path) {
        return uri(format(template, path));
    }

}