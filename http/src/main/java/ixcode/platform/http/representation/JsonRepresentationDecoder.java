package ixcode.platform.http.representation;

import ixcode.platform.serialise.JsonDeserialiser;
import ixcode.platform.serialise.KindToClassMap;

import static ixcode.platform.serialise.KindToClassMap.map;

public class JsonRepresentationDecoder extends JsonDeserialiser  {


    public JsonRepresentationDecoder(KindToClassMap kindToClassMap) {
        super(kindToClassMap);
    }

    public JsonRepresentationDecoder(String kind, Class<?> targetClass) {
        super(map().kind(kind).to(targetClass).build());
    }
}