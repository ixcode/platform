package ixcode.platform.http.representation;

import ixcode.platform.json.JsonObject;
import ixcode.platform.serialise.TransformToJson;

import java.util.List;

import static ixcode.platform.json.JsonObjectBuilder.jsonObjectWith;
import static ixcode.platform.serialise.TransformToJson.jsonObjectNameFor;

/**
 * @todo invert inheritance to composition and implement an interface for transform
 */
public class TransformRepresentationToJson extends TransformToJson {

    @Override public <T, R> R from(T object) {
        if (object instanceof Representation) {
            return (R) buildJsonObjectFrom((Representation) object);
        }
        return super.from(object);
    }

    public JsonObject buildJsonObjectFrom(Representation representation) {

        JsonObject containerJson = super.from(representation.getEntity());
        JsonObject entityJson = containerJson.valueOf(jsonObjectNameFor(representation.getEntity()));

        for (String relation : representation.getAvailableRelations()) {
            List<Hyperlink> hyperlink = representation.getHyperlinksMatching(relation);
            JsonObject linkObject = jsonObjectWith()
                    .key("href").value(hyperlink.get(0).uri)
                    .build();

            entityJson.appendJsonObject(hyperlink.get(0).relation, linkObject);
        }

        return containerJson;

    }
}