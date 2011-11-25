package ixcode.platform.http.representation;

import ixcode.platform.serialise.JsonDeserialiser;
import ixcode.platform.serialise.KindToClassMap;

public class JsonRepresentationDecoder extends JsonDeserialiser  {


    public JsonRepresentationDecoder(KindToClassMap kindToClassMap) {
        super(kindToClassMap);
    }
}