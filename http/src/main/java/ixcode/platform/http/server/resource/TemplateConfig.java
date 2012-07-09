package ixcode.platform.http.server.resource;

import ixcode.platform.di.InjectionContext;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TemplateConfig {
    public final Map<String, Object> get;
    public final Map<String, Object> post;

    TemplateConfig(Map<String, Object> get) {
        this(get, new HashMap<String, Object>());
    }

    TemplateConfig(Map<String, Object> get,
                   Map<String, Object> post) {
        this.get = get;
        this.post = post;
    }

    public TemplatedPage toEntry(String path, String templateName,
                                 File sourceFile,
                                 InjectionContext injectionContext) {
        String pathWithParameters = addParametersTo(path);

        return new TemplatedPage(pathWithParameters,
                                 templateName,
                                 sourceFile,
                                 (Map<String, Object>) get.get("data"),
                                 loadDataProviders((List<String>) get.get("data-providers"), injectionContext),
                                 loadDataConsumers((List<String>) post.get("data-consumers"), injectionContext),
                                 (String) post.get("redirect-to"),
                                 injectionContext);
    }

    private String addParametersTo(String path) {
        List<String> parameters = (List<String>)get.get("parameters");

        if (parameters == null) {
            return path;
        }

        StringBuilder sb = new StringBuilder(path);

        for (String parameter : parameters) {
            sb.append("/{").append(parameter).append("}");
        }
        return sb.toString();
    }

    private static List<DataProvider> loadDataProviders(List<String> providerNames, InjectionContext di) {
        List<DataProvider> providers =  new ArrayList<DataProvider>();
        if (providerNames == null) {
            return providers;
        }

        for (String providerName : providerNames) {
            providers.add(di.<DataProvider>getA(providerName.replaceAll("/", ".")));
        }

        return providers;
    }

    private static List<DataConsumer> loadDataConsumers(List<String> consumerNames, InjectionContext di) {
        List<DataConsumer> consumers =  new ArrayList<DataConsumer>();
        if (consumerNames == null) {
            return consumers;
        }

        for (String consumerName : consumerNames) {
            consumers.add(di.<DataConsumer>getA(consumerName.replaceAll("/", ".")));
        }

        return consumers;
    }
}