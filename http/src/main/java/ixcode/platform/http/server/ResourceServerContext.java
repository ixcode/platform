package ixcode.platform.http.server;

import ixcode.platform.collection.MapBuilder;
import ixcode.platform.di.InjectionContext;
import ixcode.platform.http.server.resource.RouteMap;
import ixcode.platform.http.server.routing.ResourceRoute;
import ixcode.platform.repository.Repository;
import ixcode.platform.repository.Resource;
import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ixcode.platform.collection.MapBuilder.linkedHashMapWith;
import static ixcode.platform.http.server.resource.RouteMap.aResourceMap;
import static ixcode.platform.repository.LinkedHashMapRepository.createRepositoryFor;
import static ixcode.platform.repository.LinkedHashMapRepository.repositoryIdFor;
import static java.lang.String.format;

public class ResourceServerContext {

    private static final Logger log = Logger.getLogger(ResourceServerContext.class);

    private final InjectionContext injectionContext = new InjectionContext();
    private final Map<String, Repository<?>> repositoryMap;


    public ResourceServerContext(Map<String, Repository<?>> repositoryMap) {
        this.repositoryMap = repositoryMap;
        registerRouteMap(injectionContext, this.repositoryMap);
    }

    private static RouteMap registerRouteMap(InjectionContext injectionContext, Map repositoryMap) {

        injectionContext.register(new ResourceRoute(repositoryMap));

        RouteMap hyperRouteMap = aResourceMap()
                .withInjectionContext(injectionContext)
                .thePath("/{resourceType}").toThe(ResourceRoute.class)
                .thePath("/{resourceType}/{resourceIdentifier}/**").toThe(ResourceRoute.class);

        injectionContext.register(hyperRouteMap); return hyperRouteMap;
    }

    public static Map<String, Repository<?>> reflectivelyBuildRepositoryMap(String rootDomainPackage) {
        log.info("Configuring resources in package " + rootDomainPackage);

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                                                          .setUrls(ClasspathHelper.forPackage(rootDomainPackage))
                                                          .setScanners(new TypeAnnotationsScanner()));


        Set<Class<?>> resourceTypes = reflections.getTypesAnnotatedWith(Resource.class);

        List<Repository<?>> repositories = new ArrayList<Repository<?>>();
        for (Class<?> resourceType : resourceTypes) {
            repositories.add(createRepositoryFor(resourceType));
        }

        return buildRepositoryMapFrom(repositories);
    }

    public static Map buildRepositoryMapFrom(List<Repository<?>> repositories) {
        MapBuilder repositoryMapBuilder = linkedHashMapWith();
        for (Repository<?> repository : repositories) {
            repositoryMapBuilder.key(repository.getRepositoryId()).value(repository);
            log.info(format("Repository: %s", repository.getRepositoryId()));
        }

        return repositoryMapBuilder.build();
    }


    public RouteMap theRouteMap() {
        return injectionContext.getThe(RouteMap.class);
    }

    public Map<String, Repository<?>> theRepositoryMap() {
        return repositoryMap;
    }

    public static <T> Repository<T> repositoryFor(Map<String, Repository<?>> repositoryMap, Class<T> itemClass) {
        return (Repository<T>) repositoryMap.get(repositoryIdFor(itemClass));
    }
}