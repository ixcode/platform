package ixcode.platform.http.server.resource;

import ixcode.platform.di.InjectionContext;

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

    public TemplatedPage toEntry(String path, String templateName,
                                 File sourceFile,
                                 InjectionContext injectionContext) {
        return new TemplatedPage(path,
                                 templateName,
                                 sourceFile,
                                 (Map<String, Object>) get.get("data"),
                                 loadDataProviders((List<String>) get.get("data-providers"), injectionContext),
                                 loadDataConsumers((List<String>) get.get("data-providers"), injectionContext),
                                 (String) post.get("redirect-to"),
                                 injectionContext);
    }

    private static List<DataProvider> loadDataProviders(List<String> providerNames, InjectionContext di) {
        List<DataProvider> providers =  new ArrayList<DataProvider>();

        for (String providerName : providerNames) {
            providers.add(di.<DataProvider>getA(providerName.replaceAll("/", ".")));
        }

        return providers;
    }

    private static List<DataConsumer> loadDataConsumers(List<String> consumerNames, InjectionContext injectionContext) {
        return new ArrayList<DataConsumer>();
    }
}