package ixcode.platform.http.server.routing;

import ixcode.platform.collection.Action;
import ixcode.platform.collection.MapBuilder;
import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.protocol.request.RequestParameter;
import ixcode.platform.http.protocol.response.ResponseBuilder;
import ixcode.platform.http.protocol.response.ResponseLinkBuilder;
import ixcode.platform.http.representation.VanillaHypermedia;
import ixcode.platform.http.server.resource.GetResource;
import ixcode.platform.http.server.resource.PostResource;
import ixcode.platform.http.server.resource.ResourceHyperlinkBuilder;
import ixcode.platform.reflect.FieldReflector;
import ixcode.platform.reflect.ObjectReflector;
import ixcode.platform.repository.PreviewAttribute;
import ixcode.platform.repository.Repository;
import ixcode.platform.repository.RepositoryItem;
import ixcode.platform.repository.RepositoryKey;
import ixcode.platform.repository.RepositorySearch;
import ixcode.platform.serialise.JsonMetadata;
import ixcode.platform.text.format.UriFormat;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static ixcode.platform.collection.MapBuilder.linkedHashMapWith;
import static ixcode.platform.http.representation.VanillaHypermedia.hypermedia;
import static ixcode.platform.reflect.ObjectReflector.reflect;
import static ixcode.platform.text.format.ArrayFormat.arrayToString;
import static java.lang.String.format;

public class ResourceRoute implements GetResource, PostResource {

    private final UriFormat uriFormat = new UriFormat();
    private final JsonMetadata jsonMetadata = new JsonMetadata();

    private final Map<String, Repository<?>> repositoryLookup;

    public ResourceRoute(Map<String, Repository<?>> repositoryLookup) {
        this.repositoryLookup = repositoryLookup;
    }

    @Override
    public void GET(Request request, ResponseBuilder respondWith, ResourceHyperlinkBuilder hyperlinkBuilder) {
        String resourceType = request.parameters.getFirstValueOf("resourceType");
        String resourceIdentifier = request.parameters.getFirstValueOf("resourceIdentifier");

        Repository repository = getRepositoryForType(resourceType);

        if (repository == null) {
            respondWithDebug(request, respondWith);
            return;
        }

        if (resourceIdentifier == null) {
            if (jsonMetadata.previewList(repository.getItemType())) {
                respondWithPreviewList(request, respondWith, resourceType, repository);
            } else {
                respondWithListOfLinks(request, respondWith, resourceType, repository);
            }
            return;
        }

        respondWithSingleItem(respondWith, resourceIdentifier, repository);
    }

    @Override
    public void POST(Request request, ResponseBuilder respondWith, ResourceHyperlinkBuilder hyperlinkBuilder) {
        String resourceType = request.parameters.getFirstValueOf("resourceType");
        String resourceIdentifier = request.parameters.getFirstValueOf("resourceIdentifier");

        Repository repository = getRepositoryForType(resourceType);

        if (repository == null) {
            respondWithDebug(request, respondWith);
            return;
        }

        if (resourceIdentifier != null) {
            throw new RuntimeException("Don't handle anything other than querys at the moment");
        }

        if (!jsonMetadata.isQueryable(repository.getItemType())) {
            respondWith.body("{ \"is\" : \"error\", \"message\" : \"Cannot query this resource, sorry\" }")
                    .contentType().json()
                    .status().badRequest();
            return;
        }

        if (jsonMetadata.previewList(repository.getItemType())) {
            respondWithPreviewList(request, respondWith, resourceType, repository);
        } else {
            respondWithListOfLinks(request, respondWith, resourceType, repository);
        }


    }

    private void respondWithPreviewList(Request request, ResponseBuilder respondWith,
                                        String resourceType, Repository repository) {

        RepositorySearch search = new RepositorySearch();
        List<RepositoryItem> results = repository.find(search);

        ResponseLinkBuilder responseLinkBuilder = respondWith.linkBuilder;

        ObjectReflector reflector = reflect(repository.getItemType());

        List<Map<String, Object>> previewItems = new ArrayList<Map<String, Object>>();
        for (final RepositoryItem repositoryItem : results) {
            String path = format("/%s/%s", resourceType, repositoryItem.repositoryKey.toString());
            final MapBuilder<String, Object> mapBuilder = linkedHashMapWith()
                    .key("self").value(responseLinkBuilder.linkTo(path));

            reflector.withEachFieldHavingAnnotation(PreviewAttribute.class, new Action<FieldReflector>() {

                @Override public void to(FieldReflector fieldReflector, Collection<FieldReflector> tail) {
                    mapBuilder.key(fieldReflector.name).value(fieldReflector.valueFrom(repositoryItem.value));
                }

            });

            previewItems.add(mapBuilder.build());
        }

        String[] tags = jsonMetadata.tagsFor(repository.getItemType());
        String queryable = (jsonMetadata.isQueryable(repository.getItemType())) ? "queryable" : "";
        VanillaHypermedia hypermedia = hypermedia(tags[0], "preview", queryable, "list");
        hypermedia.havingValue(previewItems).as("items");

        respondWith.status().ok()
                   .hypermedia(hypermedia.build());

    }

    private void respondWithSingleItem(ResponseBuilder respondWith, String resourceIdentifier, Repository repository) {
        RepositoryKey key = new RepositoryKey(repository.getRepositoryId(), resourceIdentifier);
        Object item = repository.get(key);

        String[] tags = jsonMetadata.tagsFor(repository.getItemType());
        VanillaHypermedia hypermedia = hypermedia(tags)
                .havingLinkBuilder(respondWith.linkBuilder)
                .havingRootObject(item);


        respondWith.status().ok()
                   .hypermedia(hypermedia.build());
    }

    private void respondWithListOfLinks(Request request, ResponseBuilder respondWith, String resourceType, Repository repository) {
        RepositorySearch search = new RepositorySearch();
        List<RepositoryItem> results = repository.find(search);

        ResponseLinkBuilder responseLinkBuilder = respondWith.linkBuilder;

        List<URI> links = new ArrayList<URI>();
        for (RepositoryItem item : results) {
            String path = format("/%s/%s", resourceType, item.repositoryKey.toString());
            links.add(responseLinkBuilder.linkTo(path));
        }

        String[] tags = jsonMetadata.tagsFor(repository.getItemType());
        VanillaHypermedia hypermedia = hypermedia(tags[0], "list");
        hypermedia.havingValue(links).as("items");

        respondWith.status().ok()
                   .hypermedia(hypermedia.build());

    }

    private Repository getRepositoryForType(String resourceType) {
        return repositoryLookup.get(resourceType);
    }

    private void respondWithDebug(Request request, ResponseBuilder respondWith) {

        final VanillaHypermedia hypermedia = hypermedia("generic", "resource")
                .havingValue(request.subpath).as("subpath");

        request.parameters.apply(new Action<RequestParameter>() {
            @Override
            public void to(RequestParameter item, Collection<RequestParameter> tail) {
                hypermedia.havingValue(arrayToString(item.parameterValues)).as(item.name + "{" + item.source.name() + "}");
            }
        });

        respondWith.status().ok()
                   .hypermedia(hypermedia.build());
    }


}