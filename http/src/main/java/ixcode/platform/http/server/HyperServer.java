package ixcode.platform.http.server;

import ixcode.platform.di.InjectionContext;
import ixcode.platform.http.server.redirection.RedirectTrailingSlashes;
import ixcode.platform.http.server.resource.ResourceMap;
import ixcode.platform.http.server.routing.ResourceRoute;

import static ixcode.platform.http.server.RequestDispatcher.requestDispatcher;
import static ixcode.platform.http.server.resource.ResourceMap.aResourceMapRootedAt;
import static ixcode.platform.logging.ConsoleLog4jLogging.initialiseLog4j;
import static java.lang.String.format;

public class HyperServer {

    private final InjectionContext injectionContext;
    private final String hostname;
    private final int port;
    private final InjectionContextConfiguration injectionContextConfiguration;
    private final ResourceMapConfiguration resourceMapConfiguration;


    public HyperServer(String hostname, int port,
                       InjectionContextConfiguration injectionContextConfiguration,
                       ResourceMapConfiguration resourceMapConfiguration ) {

        this.hostname = hostname;
        this.port = port;
        this.injectionContextConfiguration = injectionContextConfiguration;
        this.resourceMapConfiguration = resourceMapConfiguration;
        initialiseLog4j();
        injectionContext = new InjectionContext();
        injectionContextConfiguration.populateInjectionContext(injectionContext);
    }



    public void start() {
        RequestDispatcher requestDispatcher = requestDispatcher(withResourceMap());

        new HttpServer(this.getClass().getSimpleName(), port, requestDispatcher)
                .withRedirection(new RedirectTrailingSlashes())
                .servingStaticContentFrom("./")
                .start();
    }

    public ResourceMap withResourceMap() {
        ResourceMap map = aResourceMapRootedAt(format("http://%s:%s", hostname, port))
                .withInjectionContext(injectionContext)
                .mapping("/{resourceType}").toThe(ResourceRoute.class)
                .mapping("/{resourceType}/{resourceIdentifier}/**").toThe(ResourceRoute.class);
        
        resourceMapConfiguration.populateResourceMap(map);
        
        return map;
                
    }



}