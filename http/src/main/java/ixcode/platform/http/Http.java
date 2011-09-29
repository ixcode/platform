package ixcode.platform.http;


import ixcode.platform.http.representation.*;
import org.apache.log4j.*;

import java.net.*;

import static ixcode.platform.http.protocol.UriFactory.*;
import static ixcode.platform.io.StreamHandling.*;

public class Http {

    private static final Logger log = Logger.getLogger(Http.class);

    public <T> PostRequest<T> POST(T entity) {
        return new PostRequest<T>(entity);
    }

    public GetRepresentationRequest GET(Class<?> entityClass) {
        return new GetRepresentationRequest(entityClass);
    }

    public static class GetRepresentationRequest {

        private Class<?> entityClass;

        public GetRepresentationRequest(Class<?> entityClass) {
            this.entityClass = entityClass;
        }

        public Representation from(String uriString) {
            return from(uri(uriString));
        }

        public Representation from(URI uri) {
            try {
                log.info("GET: " + uri.toURL().toExternalForm());
                HttpURLConnection urlConnection = (HttpURLConnection) uri.toURL().openConnection();
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                if (responseCode < 200 || responseCode > 299) {
                    throw new RuntimeException("Could not complete request! Response code: " + responseCode);
                }
                String responseBody = readFully(urlConnection.getInputStream(), "UTF-8");
                log.info(responseCode + ": \n" + responseBody);
                return new RepresentationDecoder(entityClass).representationFrom(responseBody);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class PostRequest<T> {

        public PostRequest(T entity) {

        }

        public <T extends Response> T to(URI productCatalogLink) {
            return null;
        }
    }
}