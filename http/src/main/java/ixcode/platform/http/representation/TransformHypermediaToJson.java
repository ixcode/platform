package ixcode.platform.http.representation;

import ixcode.platform.json.JsonObject;
import ixcode.platform.serialise.TransformToJson;
import ixcode.platform.text.format.UriFormat;

import static ixcode.platform.json.JsonObjectBuilder.jsonObjectWith;

public class TransformHypermediaToJson extends TransformToJson {

    UriFormat uriFormat = new UriFormat();

    @Override protected Object jsonValueOf(Object value) {
        if (value instanceof Hyperlink) {
            Hyperlink hyperlink = (Hyperlink)value;
            if (hyperlink.relation == null) {
                return uriFormat.format(hyperlink.uri);
            }
            return jsonObjectWith().key(hyperlink.relation).value(uriFormat.format(hyperlink.uri));
        }
        return super.jsonValueOf(value);
    }
}