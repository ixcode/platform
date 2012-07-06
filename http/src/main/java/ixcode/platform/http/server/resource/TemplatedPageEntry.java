package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.HttpMethod;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplatedPageEntry {
    public String path = "/subsection/some-page";
    public HttpMethod[] methods = {HttpMethod.GET, HttpMethod.POST};
    public String templateName = "subsection/some-page";
    public Map<String, Object> data = new HashMap<String, Object>();
    public URI redirectUri;
    public List<DataProvider> dataProviders;

    public List<DataConsumer> dataConsumers;
}