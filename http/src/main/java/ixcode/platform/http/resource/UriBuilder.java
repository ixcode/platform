package ixcode.platform.http.resource;

import java.net.*;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;

public class UriBuilder {
    private URI baseUri;

    public static UriBuilder aUriBasedAt(URI baseUri) {
        return new UriBuilder(baseUri);
    }

    public static UriBuilder uriBasedAt(String baseUri) {
        try {
            return new UriBuilder(new URI(baseUri));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private UriBuilder(URI baseUri) {
        this.baseUri = baseUri;
    }

    public URI createUniqueUri() {
        return appendPathToBaseUri(randomUUID().toString());
    }

    private URI appendPathToBaseUri(String path) {
        try {
            return new URI(addTrailingSlash(baseUri.toURL().toExternalForm()) + path);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(format("Could not build a URI based on [%s] and [%s]", baseUri, path), e);
        }
    }

    private static String addTrailingSlash(String path) {
        return (path.endsWith("/")) ? path : path + "/";
    }

    public UriBuilder withParameter(String parameterName, String parameterValue) {
        return this;
    }

    public URI build() {
        return null;
    }
}