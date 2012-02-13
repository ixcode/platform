package ixcode.platform.http.server;

import ixcode.platform.collection.MapBuilder;
import ixcode.platform.di.InjectionContext;
import ixcode.platform.http.server.redirection.RedirectTrailingSlashes;
import ixcode.platform.http.server.resource.RouteMap;
import ixcode.platform.http.server.routing.ResourceRoute;
import ixcode.platform.repository.Repository;

import static ixcode.platform.collection.MapBuilder.linkedHashMapWith;
import static ixcode.platform.http.server.RequestDispatcher.requestDispatcher;
import static ixcode.platform.http.server.resource.RouteMap.aResourceMapRootedAt;
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
                       ResourceMapConfiguration resourceMapConfiguration) {

        this.hostname = hostname;
        this.port = port;
        this.injectionContextConfiguration = injectionContextConfiguration;
        this.resourceMapConfiguration = resourceMapConfiguration;
        initialiseLog4j();
        injectionContext = new InjectionContext();
        registerResourceRoute(injectionContextConfiguration);
        injectionContextConfiguration.populateInjectionContext(injectionContext);
    }


    public void start() {
        RequestDispatcher requestDispatcher = requestDispatcher(withResourceMap());

        new HttpServer(this.getClass().getSimpleName(), port, requestDispatcher)
                .withRedirection(new RedirectTrailingSlashes())
                .servingStaticContentFrom("./")
                .start();
    }

    public RouteMap withResourceMap() {
        RouteMap map = aResourceMapRootedAt(format("http://%s:%s", hostname, port))
                .withInjectionContext(injectionContext)
                .thePath("/{resourceType}").toThe(ResourceRoute.class)
                .thePath("/{resourceType}/{resourceIdentifier}/**").toThe(ResourceRoute.class);

        resourceMapConfiguration.populateRouteMap(map);

        return map;

    }

    private void registerResourceRoute(InjectionContextConfiguration injectionContextConfiguration) {
        MapBuilder repositoryMapBuilder = linkedHashMapWith();
        for (Repository<?> repository : injectionContextConfiguration.repositories()) {
            repositoryMapBuilder.key(repository.getRepositoryId()).value(repository);
        }

        injectionContext.register(new ResourceRoute(repositoryMapBuilder.build()));
    }


}