package ixcode.platform.http.representation;

import ixcode.platform.http.protocol.response.ResponseLinkBuilder;
import ixcode.platform.reflect.FieldReflector;
import ixcode.platform.reflect.ObjectReflector;
import ixcode.platform.repository.RepositoryKey;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ixcode.platform.collection.CollectionPrinter.printCollection;
import static ixcode.platform.http.representation.Hyperlink.hyperlinkTo;
import static ixcode.platform.reflect.ObjectReflector.reflect;
import static java.lang.String.format;
import static java.util.Arrays.asList;

public abstract class HypermediaRepresentationBuilder<T extends HypermediaRepresentationBuilder> implements LinkCollection {
    private ReflectiveMapBuilder mapBuilder = new ReflectiveMapBuilder();
    private transient List<Hyperlink> hyperlinks = new ArrayList<Hyperlink>();
    protected List<String> types;
    private List<ValuePair> valuePairs = new ArrayList<ValuePair>();
    private Object rootObject;
    private ResponseLinkBuilder linkBuilder;


    public HypermediaRepresentationBuilder(String... types) {
        this.types = new ArrayList<String>(asList(types));
    }

    public T withTypes(String... types) {
        this.types = asList(types);
        return (T) this;
    }

    private T addTypesfrom(List<String> types) {
        if (types.size() > 1) {
            mapBuilder.key("is").value(types);
        } else if (types.size() == 1) {
            mapBuilder.key("is").value(types.get(0));
        }
        return (T) this;
    }

    public T havingRootObject(Object rootObject) {
        this.rootObject = rootObject;
        return (T)this;
    }
    
    public T havingLinkBuilder(ResponseLinkBuilder linkBuilder) {
        this.linkBuilder = linkBuilder;
        return (T)this;
    }

    public KeyValueBuilder<T> havingValue(Object value) {
        return new KeyValueBuilder<T>(this, value);
    }

    private void addValuePair(ValuePair valuePair) {
        valuePairs.add(valuePair);
    }

    public Map<String, Object> build() {
        Object root = (rootObject != null) ? rootObject : this;

        return addTypesfrom(types)
                .addValuesFrom(root)
                .havingValues(valuePairs)
                .excludingNulls()
                .linkingTo(hyperlinks)
                .buildMap();
    }

    private T addValuesFrom(Object source) {
        addValuesAndLinksFrom(source);
        return (T) this;
    }
    
    private void addValuesAndLinksFrom(Object source) {
        ObjectReflector reflector = reflect(source.getClass());
        for (FieldReflector fieldReflector : reflector.nonTransientFields) {
            Object value = fieldReflector.valueFrom(source);
            if (value instanceof RepositoryKey) {
                RepositoryKey key = (RepositoryKey)value;
                URI uri = linkBuilder.linkTo(format("/%s/%s", key.repositoryId, key.key));
                mapBuilder.key(fieldReflector.name).value(uri);
            } else if (value != null) {
                mapBuilder.key(fieldReflector.name).value(value);
            }
        }        
    }

    private HypermediaRepresentationBuilder havingValues(List<ValuePair> valuePairs) {
        for (ValuePair valuePair : valuePairs) {
            mapBuilder.key(valuePair.key).value(valuePair.value);
        }
        return this;
    }

    private Map<String, Object> buildMap() {
        return mapBuilder.build();
    }

    public T excludingNulls() {
        return (T) this;
    }

    public T withHyperlink(Hyperlink hyperlink) {
        hyperlinks.add(hyperlink);
        return (T)this;
    }

    private T linkingTo(List<Hyperlink> hyperlinks) {
        for (Hyperlink hyperlink : hyperlinks) {
            mapBuilder.key(hyperlink.relation).value(hyperlink.uri);
        }
        return (T) this;
    }

    public LinkBuilder<T> linkingTo(URI uri) {
        return new LinkBuilder(this, uri);
    }

    public void addHyperlink(URI uri, String relation, String title) {
        hyperlinks.add(hyperlinkTo(uri, relation, title));
    }

    public static class KeyValueBuilder<T extends HypermediaRepresentationBuilder> {
        private final HypermediaRepresentationBuilder<T> parent;
        private final Object value;

        public KeyValueBuilder(HypermediaRepresentationBuilder<T> parent, Object value) {
            this.parent = parent;
            this.value = value;
        }

        public T as(String key) {
            parent.addValuePair(new ValuePair(key, value));
            return (T) parent;
        }
    }

    private static class ValuePair {
        public final String key;
        public final Object value;

        private ValuePair(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }
}