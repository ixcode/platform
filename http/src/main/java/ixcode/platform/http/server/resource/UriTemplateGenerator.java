package ixcode.platform.http.server.resource;

import ixcode.platform.collection.Action;
import ixcode.platform.http.representation.Hyperlink;
import ixcode.platform.http.server.resource.path.UriTemplate;
import ixcode.platform.reflect.FieldReflector;
import ixcode.platform.reflect.ObjectReflector;
import ixcode.platform.text.format.CollectionFormat;
import ixcode.platform.text.format.Format;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ixcode.platform.reflect.ObjectReflector.reflect;
import static ixcode.platform.text.format.CollectionFormat.collectionToString;

public class UriTemplateGenerator<T extends UriTemplateGenerator> {

    private final ResourceLookup source;
    private final Class<?> resourceClass;
    private String relation;
    private final ObjectReflector reflector;

    public UriTemplateGenerator(ResourceLookup source, Class<?> resourceClass, String relation) {
        this.source = source;
        this.resourceClass = resourceClass; this.relation = relation;
        reflector = reflect(this.getClass());
    }

    public T withRelation(String relation) {
        this.relation = relation;
        return (T)this;
    }

    public Hyperlink hyperlink() {
        final Object instance = this;

        List<UriTemplate> uriTemplates = source.uriTemplateMappedTo(resourceClass);

        final Map<String, String> uriParameters = new LinkedHashMap<String, String>();
        final Map<String, String> queryParameters = new LinkedHashMap<String, String>();

        reflector.nonTransientFields.apply(new Action<FieldReflector>() {
            @Override public void to(FieldReflector item, Collection<FieldReflector> tail) {
                Object value = item.valueFrom(instance);
                if (value != null) {
                    Format format = reflector.findFormatFor(value.getClass());
                    String stringValue = format.format(value);
                    if (item.hasAnnotation(QueryParameter.class)) {
                        queryParameters.put(item.name, stringValue);
                    }  else {
                        uriParameters.put(item.name, stringValue);
                    }
                }
            }
        });


        for (UriTemplate uriTemplate : uriTemplates) {
            if (uriTemplate.matches(uriParameters)) {
                return uriTemplate.hyperlinkFrom(uriParameters, queryParameters, relation);
            }
        }

        throw new RuntimeException("Could find no matches for uri templates for the template generator " + this.getClass().getName() + ", properties: " + collectionToString(uriParameters.keySet()));
    }

    public void addQueryParameter(String name, Object value) {

    }
}