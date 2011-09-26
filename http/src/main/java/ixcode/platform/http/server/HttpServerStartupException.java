package ixcode.platform.http.server;

public class HttpServerStartupException extends RuntimeException {
    public HttpServerStartupException(Exception cause) {
        super("Could not start HtppServer (see cause)", cause);
    }
}