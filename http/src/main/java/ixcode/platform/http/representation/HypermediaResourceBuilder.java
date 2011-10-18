package ixcode.platform.http.representation;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static ixcode.platform.collection.CollectionPrinter.printCollection;
import static ixcode.platform.http.representation.Hyperlink.hyperlinkTo;
import static java.util.Arrays.asList;

public class HypermediaResourceBuilder<T extends LinkCollection> implements LinkCollection {
    private ReflectiveMapBuilder mapBuilder = new ReflectiveMapBuilder();
    private transient List<Hyperlink> hyperlinks = new ArrayList<Hyperlink>();
    protected List<String> types;

    public HypermediaResourceBuilder(String... types) {
        this.types = new ArrayList<String>(asList(types));
    }

    public HypermediaResourceBuilder withTypes(List<String> types) {
        mapBuilder.key("is").value(printCollection(types, " "));
        return this;
    }

    public HypermediaResourceBuilder havingValuesFrom(Object source) {
        mapBuilder.extractValuesFrom(source);
        return this;
    }


    public Map<String, Object> build() {
        return withTypes(types)
                .havingValuesFrom(this)
                .excludingNulls()
                .linkingTo(hyperlinks)
                .buildMap();
    }

    private Map<String, Object> buildMap() {
        return mapBuilder.build();
    }

    public HypermediaResourceBuilder excludingNulls() {
        return this;
    }

    public HypermediaResourceBuilder withLink(URI uri, String relation, String title) {
        mapBuilder.key(relation).value(hyperlinkTo(uri, relation, title));
        return this;
    }

    public HypermediaResourceBuilder linkingTo(List<Hyperlink> hyperlinks) {
        for (Hyperlink hyperlink : hyperlinks) {
            mapBuilder.key(hyperlink.relation).value(hyperlink.uri);
        }
        return this;
    }

    public LinkBuilder<T> linkingTo(URI uri) {
        return new LinkBuilder(this, uri);
    }

    public void addHyperlink(URI uri, String relation, String title) {
        hyperlinks.add(hyperlinkTo(uri, relation, title));
    }
}