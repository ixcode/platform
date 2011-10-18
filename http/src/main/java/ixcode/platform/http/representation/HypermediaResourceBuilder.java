package ixcode.platform.http.representation;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ixcode.platform.collection.CollectionPrinter.printCollection;
import static ixcode.platform.http.representation.Hyperlink.hyperlinkTo;
import static java.util.Arrays.asList;

public class HypermediaResourceBuilder<T extends HypermediaResourceBuilder> implements LinkCollection {
    private ReflectiveMapBuilder mapBuilder = new ReflectiveMapBuilder();
    private transient List<Hyperlink> hyperlinks = new ArrayList<Hyperlink>();
    protected List<String> types;
    private List<ValuePair> valuePairs = new ArrayList<ValuePair>();

    public HypermediaResourceBuilder(String... types) {
        this.types = new ArrayList<String>(asList(types));
    }

    public T withTypes(List<String> types) {
        mapBuilder.key("is").value(printCollection(types, " "));
        return (T) this;
    }

    public T havingValuesFrom(Object source) {
        mapBuilder.extractValuesFrom(source);
        return (T) this;
    }

    public KeyValueBuilder<T> havingValue(Object value) {
        return new KeyValueBuilder<T>(this, value);
    }

    private void addValuePair(ValuePair valuePair) {
        valuePairs.add(valuePair);
    }


    public Map<String, Object> build() {
        return withTypes(types)
                .havingValuesFrom(this)
                .havingValues(valuePairs)
                .excludingNulls()
                .linkingTo(hyperlinks)
                .buildMap();
    }

    private HypermediaResourceBuilder havingValues(List<ValuePair> valuePairs) {
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

    public static class KeyValueBuilder<T extends HypermediaResourceBuilder> {
        private final HypermediaResourceBuilder<T> parent;
        private final Object value;

        public KeyValueBuilder(HypermediaResourceBuilder<T> parent, Object value) {
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