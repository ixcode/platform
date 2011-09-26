package ixcode.platform.http;


import ixcode.platform.http.representation.*;
import org.apache.log4j.*;

import java.net.*;

public class Http {

    private static final Logger log = Logger.getLogger(Http.class);

    public <T> PostMethod<T> POST(T entity) {
        return new PostMethod<T>(entity);
    }

    public <T> RepresentationDecoder GET(Class<T> entityClass) {
        return new RepresentationDecoder<T>(entityClass);
    }

    public static class PostMethod<T> {

        public PostMethod(T entity) {

        }

        public <T extends Response> T to(URI productCatalogLink) {
            return null;
        }
    }
}