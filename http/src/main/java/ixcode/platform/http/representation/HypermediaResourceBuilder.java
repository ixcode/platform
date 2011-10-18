package ixcode.platform.http.representation;

import com.sun.tools.internal.xjc.reader.xmlschema.WildcardNameClassBuilder;
import ixcode.platform.collection.CollectionPrinter;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static ixcode.platform.collection.CollectionPrinter.printCollection;
import static java.util.Arrays.asList;

public class HypermediaResourceBuilder {
    private ReflectiveMapBuilder mapBuilder = new ReflectiveMapBuilder();

    public static HypermediaResourceBuilder hypermedia() {
        return new HypermediaResourceBuilder();
    }

    public HypermediaResourceBuilder withTypes(String... types) {
        mapBuilder.key("is").value(printCollection(asList(types), " "));
        return this;
    }

    public HypermediaResourceBuilder havingValuesFrom(Object source) {
        mapBuilder.extractValuesFrom(source);
        return this;
    }

    public Map<String, Object> build() {
        return mapBuilder.build();
    }

    public HypermediaResourceBuilder excludingNulls() {
        return this;
    }

    public HypermediaResourceBuilder withLink(URI uri, String relation, String title) {
        mapBuilder.key(relation).value(new Hyperlink(uri, relation, title));
        return this;
    }

    public HypermediaResourceBuilder linkingTo(List<Hyperlink> hyperlinks) {
        for (Hyperlink hyperlink : hyperlinks) {
            mapBuilder.key(hyperlink.relation).value(hyperlink.uri);
        }
        return this;
    }
}