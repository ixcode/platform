package ixcode.platform.http.server.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class TemplateConfig {
    public final Map<String, Object> get;
    public final Map<String, Object> post;


    TemplateConfig(Map<String, Object> get,
                   Map<String, Object> post) {
        this.get = get;
        this.post = post;
    }

    public TemplatedPageEntry toEntry(String path, String templateName, File sourceFile) {
        return new TemplatedPageEntry(path,
                                      templateName,
                                      sourceFile,
                                      (Map<String, Object>)get.get("data"),
                                      loadDataProviders((List<String>)get.get("data-providers")),
                                      loadDataConsumers((List<String>)get.get("data-providers")),
                                      (String)post.get("redirect-to"));
    }

    private static List<DataProvider> loadDataProviders(List<String> providerNames) {
        return new ArrayList<DataProvider>();
    }
    private static List<DataConsumer> loadDataConsumers(List<String> consumerNames) {
        return new ArrayList<DataConsumer>();
    }
}