package ixcode.platform.http.representation;

import ixcode.platform.serialise.TransformToJson;
import ixcode.platform.text.format.UriFormat;

import java.net.URI;
import java.net.URL;

import static ixcode.platform.json.JsonObjectBuilder.jsonObjectWith;

public class TransformHypermediaToJson extends TransformToJson {

    UriFormat uriFormat = new UriFormat();

    @Override protected Object jsonValueOf(Object value) {
        if (value instanceof Hyperlink) {
            Hyperlink hyperlink = (Hyperlink)value;
            if (hyperlink.relation == null) {
                return uriFormat.format(hyperlink.uri);
            }
            return jsonObjectWith().key(hyperlink.relation).value(uriFormat.format(hyperlink.uri)).build();
        } else if (value instanceof URI) {
            return uriFormat.format((URI)value);
        }  else if (value instanceof URL) {
            try {
                return uriFormat.format(((URL)value).toURI());
            } catch (Exception e) {
                throw new RuntimeException("Couldn not format URL: " + value + " (See Cause)", e);
            }
        }
        return super.jsonValueOf(value);
    }
}