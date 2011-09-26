package ixcode.platform.http;

import java.net.*;

public class UriFactory {

    public static URI uri(String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}