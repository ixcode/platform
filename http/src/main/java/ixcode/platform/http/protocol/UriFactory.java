package ixcode.platform.http.protocol;

import java.net.*;

import static java.lang.String.format;

public class UriFactory {

    public static URI uri(String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static String externalFormOf(URI uri) {
        try {
            return uri.toURL().toExternalForm();
        } catch (MalformedURLException e) {
            throw new RuntimeException(format("Could not get external form of [%s] (see cause)", uri), e);
        }
    }
}