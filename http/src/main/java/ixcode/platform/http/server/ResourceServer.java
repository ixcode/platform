package ixcode.platform.http.server;

import ixcode.platform.http.server.redirection.RedirectTrailingSlashes;
import ixcode.platform.http.server.resource.RouteMap;
import ixcode.platform.repository.Repository;

import java.util.Map;

import static ixcode.platform.http.server.RequestDispatcher.requestDispatcher;
import static ixcode.platform.http.server.ResourceServerContext.reflectivelyBuildRepositoryMap;
import static ixcode.platform.logging.ConsoleLog4jLogging.initialiseLog4j;
import static java.lang.String.format;

public class ResourceServer {

    private final String hostname;
    private final int port;
    private final RouteMap routeMap;
    private ResourceServerContext context;


    public ResourceServer(String hostname, int port, String rootResourcePackageName) {
        this(hostname, port, new ResourceServerContext(reflectivelyBuildRepositoryMap(rootResourcePackageName)));
    }
    
    private ResourceServer(String hostname, int port, ResourceServerContext resourceServerContext) {
        this.context = resourceServerContext;
        this.hostname = hostname;
        this.port = port;
        this.routeMap = context.theRouteMap().rootedAt(format("http://%s:%s", hostname, port));
        initialiseLog4j();
    }


    public void start() {
        RequestDispatcher requestDispatcher = requestDispatcher(format("http://%s:%s", hostname, port), routeMap);

        new HttpServer(this.getClass().getSimpleName(), port, requestDispatcher)
                .withRedirection(new RedirectTrailingSlashes())
                .servingStaticContentFrom("./")
                .start();
    }


    public ResourceServerContext getContext() {
        return context;
    }
}