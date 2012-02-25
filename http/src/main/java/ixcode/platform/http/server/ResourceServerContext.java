package ixcode.platform.http.server;

import ixcode.platform.di.InjectionContext;
import ixcode.platform.http.server.resource.RouteMap;
import ixcode.platform.http.server.routing.ResourceRoute;

import java.util.Map;

import static ixcode.platform.http.server.resource.RouteMap.aResourceMap;

public class ResourceServerContext {

    private final InjectionContext injectionContext = new InjectionContext();


    public ResourceServerContext(Map repositoryMap) {
        RouteMap hyperRouteMap = registerRouteMap(injectionContext, repositoryMap);

    }

    private static RouteMap registerRouteMap(InjectionContext injectionContext, Map repositoryMap) {

        injectionContext.register(new ResourceRoute(repositoryMap));

        RouteMap hyperRouteMap = aResourceMap()
                .withInjectionContext(injectionContext)
                .thePath("/{resourceType}").toThe(ResourceRoute.class)
                .thePath("/{resourceType}/{resourceIdentifier}/**").toThe(ResourceRoute.class);

        injectionContext.register(hyperRouteMap); return hyperRouteMap;
    }


    public RouteMap theRouteMap() {
        return injectionContext.getThe(RouteMap.class);
    }
}