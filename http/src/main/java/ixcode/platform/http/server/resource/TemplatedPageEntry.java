package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.HttpMethod;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ixcode.platform.collection.MapBuilder.linkedHashMapWith;

public class TemplatedPageEntry {
    public final String path;
    public final String templateName;

    public final HttpMethod[] methods = {HttpMethod.GET, HttpMethod.POST};

    public final Map<String, Object> data = linkedHashMapWith()
            .key("title").value("A Dynamic page")
            .key("message").value("Seek and Ye shall find")
            .build();

    public final URI redirectUri = null;
    public final List<DataProvider> dataProviders = new ArrayList<DataProvider>();
    public final List<DataConsumer> dataConsumers = new ArrayList<DataConsumer>();

    public TemplatedPageEntry(String path, String templateName) {
        this.path = path;
        this.templateName = templateName;
    }
}