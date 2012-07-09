package ixcode.platform.http.server.resource;

import ixcode.platform.text.format.UriFormat;

import java.net.URI;

public class Redirection {
    private final String requestUrl;
    private final String requestPath;

    public static Redirection redirect(String requestUrl, String requestPath) {
        return new Redirection(requestUrl, requestPath);
    }

    Redirection(String requestUrl, String requestPath) {
        this.requestUrl = requestUrl;
        this.requestPath = requestPath;
    }

    public URI to(String redirectPath) {
        String redirectTo = redirectPath;
        if (redirectPath.startsWith(".")) {
            redirectTo = requestPath.substring(0, requestPath.lastIndexOf("/")) + redirectPath.substring(1);
        }
        String urlRoot = requestUrl.substring(0, requestUrl.length() - requestPath.length());
        return new UriFormat().parseString(urlRoot + redirectTo);
    }
}