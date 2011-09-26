package ixcode.platform.http.representation;

import ixcode.platform.xml.*;
import org.apache.log4j.*;

import java.net.*;

import static ixcode.platform.http.protocol.UriFactory.*;
import static ixcode.platform.io.StreamHandling.*;

public class RepresentationDecoder<T> {
    private static final Logger log = Logger.getLogger(RepresentationDecoder.class);

    private Class<T> entityClass;

    public RepresentationDecoder(Class<T> entityClass) {
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

    private static <T> Representation representationFrom(String responseMessage, Class<T> entityClass) {
        RepresentationHandler<T> handler = new RepresentationHandler<T>(entityClass);
        new XmlParser().parse(responseMessage).using(handler);
        return handler.buildRepresentation();
    }
}