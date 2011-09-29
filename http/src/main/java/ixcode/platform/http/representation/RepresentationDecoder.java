package ixcode.platform.http.representation;

import ixcode.platform.xml.*;
import org.apache.log4j.*;

import java.net.*;

import static ixcode.platform.http.protocol.UriFactory.*;
import static ixcode.platform.io.StreamHandling.*;

public class RepresentationDecoder {
    private static final Logger log = Logger.getLogger(RepresentationDecoder.class);

    private Class<?> entityClass;

    public RepresentationDecoder(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public <T> Representation representationFrom(String responseMessage) {
        RepresentationHandler<T> handler = new RepresentationHandler<T>((Class<T>)entityClass);
        new XmlParser().parse(responseMessage).using(handler);
        return handler.buildRepresentation();
    }



}