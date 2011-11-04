package ixcode.platform.http.server.resource;

import ixcode.platform.http.representation.Hyperlink;
import ixcode.platform.http.server.resource.path.UriTemplate;
import ixcode.platform.reflect.ObjectReflector;
import ixcode.platform.text.format.CollectionFormat;

import java.util.List;
import java.util.Map;

import static ixcode.platform.reflect.ObjectReflector.reflect;
import static ixcode.platform.text.format.CollectionFormat.collectionToString;

public class UriTemplateGenerator {

    private final ResourceLookup source;
    private final Class<?> resourceClass;
    private final ObjectReflector reflector;

    public UriTemplateGenerator(ResourceLookup source, Class<?> resourceClass) {
        this.source = source;
        this.resourceClass = resourceClass;
        reflector = reflect(this.getClass());
    }

    protected Hyperlink buildHyperlink() {
        List<UriTemplate> uriTemplates = source.uriTemplateMappedTo(resourceClass);


        Map<String, String> properties = reflector.propertyValuesOf(this);

        for (UriTemplate uriTemplate : uriTemplates) {
            if (uriTemplate.matches(properties)) {
                return uriTemplate.hyperlinkFrom(properties);
            }
        }

        throw new RuntimeException("Could find no matches for uri templates for the template generator " + this.getClass().getName() + ", properties: " + collectionToString(properties.keySet()));
    }
}