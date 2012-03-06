package ixcode.platform.http.server.routing;

import ixcode.platform.collection.Action;
import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.protocol.request.RequestParameter;
import ixcode.platform.http.protocol.response.ResponseBuilder;
import ixcode.platform.http.protocol.response.ResponseLinkBuilder;
import ixcode.platform.http.representation.VanillaHypermedia;
import ixcode.platform.http.server.resource.GetResource;
import ixcode.platform.http.server.resource.PostResource;
import ixcode.platform.http.server.resource.ResourceHyperlinkBuilder;
import ixcode.platform.repository.Repository;
import ixcode.platform.repository.RepositoryItem;
import ixcode.platform.repository.RepositoryKey;
import ixcode.platform.repository.RepositorySearch;
import ixcode.platform.serialise.JsonMetadata;
import ixcode.platform.serialise.JsonSerialiser;
import ixcode.platform.text.format.UriFormat;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static ixcode.platform.http.representation.VanillaHypermedia.hypermedia;
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
            respondWithList(request, respondWith, resourceType, repository);
            return;
        }

        respondWithSingleItem(respondWith, resourceIdentifier, repository);
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

    private void respondWithList(Request request, ResponseBuilder respondWith, String resourceType, Repository repository) {
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
        hypermedia.havingValue(links).as("list");

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

    @Override
    public void POST(Request request, ResponseBuilder respondWith, ResourceHyperlinkBuilder hyperlinkBuilder) {

    }

}