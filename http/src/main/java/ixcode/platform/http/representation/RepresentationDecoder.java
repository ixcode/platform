package ixcode.platform.http.representation;

import ixcode.platform.http.protocol.response.ResponseStatus;
import ixcode.platform.xml.*;
import org.apache.log4j.*;

import java.net.*;
import java.util.List;
import java.util.Map;

import static ixcode.platform.http.protocol.UriFactory.*;
import static ixcode.platform.io.StreamHandling.*;

public class RepresentationDecoder {
    private static final Logger log = Logger.getLogger(RepresentationDecoder.class);

    private Class<?> entityClass;

    public RepresentationDecoder(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public <T> Representation representationFrom(ResponseStatus responseStatus, String responseMessage, Map<String, List<String>> httpHeaders) {
        if (entityClass == null) {
            return new RawRepresentation(responseStatus,  responseMessage, httpHeaders);
        }
        RepresentationHandler handler = new RepresentationHandler(entityClass, responseStatus, httpHeaders);
        new XmlParser().parse(responseMessage).using(handler);
        return handler.buildRepresentation();
    }

    private static class RawRepresentation extends Representation {

        public RawRepresentation(ResponseStatus responseStatus, String response, Map<String, List<String>> httpHeaders) {
            super(responseStatus, response, httpHeaders);
        }
    }


}