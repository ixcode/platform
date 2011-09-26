package ixcode.platform.http;


import org.apache.log4j.*;
import ixcode.platform.xml.*;

import java.net.*;

import static ixcode.platform.http.UriFactory.*;
import static ixcode.platform.io.StreamHandling.readFully;

public class Http {

    private static final Logger log = Logger.getLogger(Http.class);

    public <T> PostMethod<T> POST(T entity) {
        return new PostMethod<T>(entity);
    }

    public <T> RepresentationBuilder GET(Class<T> entityClass) {
        return new RepresentationBuilder<T>(entityClass);
    }


    private static <T> Representation representationFrom(String responseMessage, Class<T> entityClass) {
        RepresentationHandler<T> handler = new RepresentationHandler<T>(entityClass);
        new XmlParser().parse(responseMessage).using(handler);
        return handler.buildRepresentation();
    }

    public static class RepresentationBuilder<T> {
        private Class<T> entityClass;

        public RepresentationBuilder(Class<T> entityClass) {
            this.entityClass = entityClass;
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
                return representationFrom(responseBody, entityClass);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public Representation from(String uriString) {
            return from(uri(uriString));
        }
    }


    public static class PostMethod<T> {

        public PostMethod(T entity) {

        }

        public <T extends Response> T to(URI productCatalogLink) {
            return null;
        }
    }
}