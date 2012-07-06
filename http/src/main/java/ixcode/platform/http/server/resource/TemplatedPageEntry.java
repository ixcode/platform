package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.HttpMethod;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ixcode.platform.collection.MapBuilder.linkedHashMapWith;

public class TemplatedPageEntry {
    public final String path;
    public final String templateName;

    public final HttpMethod[] methods = {HttpMethod.GET, HttpMethod.POST};

    public final Map<String, Object> data;

    public final URI redirectUri = null;
    public final List<DataProvider> dataProviders = new ArrayList<DataProvider>();
    public final List<DataConsumer> dataConsumers = new ArrayList<DataConsumer>();

    public TemplatedPageEntry(String path, String templateName) {
        this(path, templateName, new HashMap<String, Object>());
    }

    public TemplatedPageEntry(String path, String templateName, Map<String, Object> data) {
        this.path = path;
        this.templateName = templateName;
        this.data = data;
    }
}