package ixcode.platform.http.server;

import ixcode.platform.http.protocol.ContentType;
import ixcode.platform.http.server.redirection.RedirectTrailingSlashes;
import ixcode.platform.http.server.resource.RouteMap;
import org.apache.log4j.Logger;

import static ixcode.platform.http.server.RequestDispatcher.requestDispatcher;
import static ixcode.platform.http.server.ResourceServerContext.reflectivelyBuildRepositoryMap;
import static ixcode.platform.logging.ConsoleLogging.initialise;
import static java.lang.String.format;

public class ResourceServer {

    private static final Logger log = Logger.getLogger(ResourceServer.class);

    private final String hostname;
    private final int port;
    private final ContentType defaultContentType;
    private final RouteMap routeMap;
    private final String serverName;
    private final ResourceServerContext context;
    private final String staticContentRoot;

    static {
        initialise();
    }

    public ResourceServer(String serverName,
                          String hostname, int port,
                          String rootResourcePackageName,
                          ContentType defaultcontentType,
                          String staticContentRoot) {

        this(serverName, hostname, port,
             new ResourceServerContext(reflectivelyBuildRepositoryMap(rootResourcePackageName)),
             defaultcontentType,
             staticContentRoot);
    }

    private ResourceServer(String serverName,
                           String hostname, int port,
                           ResourceServerContext resourceServerContext,
                           ContentType defaultContentType,
                           String staticContentRoot) {

        this.serverName = serverName;
        this.context = resourceServerContext;
        this.hostname = hostname;
        this.port = port;
        this.defaultContentType = defaultContentType;
        this.routeMap = context.theRouteMap().rootedAt(format("http://%s:%s", hostname, port));
        this.staticContentRoot = staticContentRoot;
    }


    public void start() {
        RequestDispatcher requestDispatcher = requestDispatcher(format("http://%s:%s", hostname, port), routeMap, defaultContentType);

        new HttpServer(serverName, hostname, port, requestDispatcher)
                .withRedirection(new RedirectTrailingSlashes())
                .servingStaticContentFrom(staticContentRoot)
                .start();
    }


    public ResourceServerContext getContext() {
        return context;
    }
}