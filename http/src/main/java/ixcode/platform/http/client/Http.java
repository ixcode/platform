package ixcode.platform.http.client;


import ixcode.platform.http.protocol.ContentTypeBuilder;
import ixcode.platform.http.representation.*;
import org.apache.log4j.*;

import java.net.*;
import java.util.List;
import java.util.Map;

import static ixcode.platform.http.protocol.UriFactory.*;
import static ixcode.platform.http.protocol.response.ResponseStatusCodes.codeToStatus;
import static ixcode.platform.io.StreamHandling.*;

public class Http {

    private static final Logger log = Logger.getLogger(Http.class);

    public <T> PostRequestBuilder<T> POST(T entity) {
        return new PostRequestBuilder<T>(entity);
    }

    public GetRepresentationRequest GET(Class<?> entityClass) {
        return new GetRepresentationRequest(entityClass);
    }

    public GetRepresentationRequest GET() {
        return new GetRepresentationRequest(null);
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
                log.info("GET " + uri.toURL().toExternalForm() + " HTTP/1.1");
                HttpURLConnection urlConnection = (HttpURLConnection) uri.toURL().openConnection();
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                String responseBody = null;
                if (responseCode < 200 || responseCode > 299) {
                    responseBody = readFully(urlConnection.getErrorStream(), "UTF-8");
                } else {
                    responseBody = readFully(urlConnection.getInputStream(), "UTF-8");
                }

                log.info("HTTP/1.1 " + responseCode + " " + urlConnection.getResponseMessage());

                if (log.isDebugEnabled()) {
                    log.debug("\n" + responseBody);
                }

                Map<String, List<String>> httpHeaders = urlConnection.getHeaderFields();

                return new RepresentationDecoder(entityClass).representationFrom(codeToStatus(responseCode), responseBody, httpHeaders);

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

        public <T> T to(URI productCatalogLink) {
            return null;
        }
    }

    public static class PostRequestBuilder<T> {

        private T entity;
        private URI uri;

        public PostRequestBuilder(T entity) {
            this.entity = entity;
        }

        public PostRequestBuilder<T> to(URI uri) {
            this.uri = uri;
            return this;
        }

        public ContentTypeBuilder as() {
            return null;
        }
    }
}