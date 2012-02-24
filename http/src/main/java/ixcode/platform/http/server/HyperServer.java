package ixcode.platform.http.server;

import ixcode.platform.http.server.redirection.RedirectTrailingSlashes;
import ixcode.platform.http.server.resource.RouteMap;

import static ixcode.platform.http.server.RequestDispatcher.requestDispatcher;
import static ixcode.platform.logging.ConsoleLog4jLogging.initialiseLog4j;
import static java.lang.String.format;

public class HyperServer {

    private final String hostname;
    private final int port;
    private final RouteMap routeMap;


    public HyperServer(String hostname, int port, HyperServerContext hyperServerContext) {
        this.hostname = hostname;
        this.port = port;
        this.routeMap = hyperServerContext.theRouteMap().rootedAt(format("http://%s:%s", hostname, port));
        initialiseLog4j();
    }


    public void start() {
        RequestDispatcher requestDispatcher = requestDispatcher(format("http://%s:%s", hostname, port), routeMap);

        new HttpServer(this.getClass().getSimpleName(), port, requestDispatcher)
                .withRedirection(new RedirectTrailingSlashes())
                .servingStaticContentFrom("./")
                .start();
    }


}