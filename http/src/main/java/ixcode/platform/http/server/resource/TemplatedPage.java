package ixcode.platform.http.server.resource;

import ixcode.platform.di.InjectionContext;
import ixcode.platform.http.protocol.HttpMethod;
import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.protocol.response.ResponseStatusBuilder;
import ixcode.platform.http.template.TemplateContext;
import ixcode.platform.serialise.JsonDeserialiser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ixcode.platform.http.template.TemplateContext.templateContext;
import static ixcode.platform.io.IoStreamHandling.readFileAsString;
import static ixcode.platform.serialise.KindToClassMap.map;
import static java.lang.String.format;

public class TemplatedPage {
    public final String path;
    public final String templateName;
    public final File sourceFile;

    public final HttpMethod[] methods = {HttpMethod.GET, HttpMethod.POST};


    public final Map<String, Object> data;

    public final List<DataProvider> dataProviders;
    public final List<DataConsumer> dataConsumers;

    public final String redirectTo;
    private InjectionContext injectionContext;

    public TemplatedPage(String path, String templateName, InjectionContext injectionContext) {
        this(path,
             templateName,
             (File) null,
             new HashMap<String, Object>(),
             new ArrayList<DataProvider>(),
             new ArrayList<DataConsumer>(),
             (String) null,
             injectionContext);
    }

    public TemplatedPage(String path,
                         String templateName,
                         File sourceFile,
                         Map<String, Object> data,
                         List<DataProvider> dataProviders,
                         List<DataConsumer> dataConsumers,
                         String redirectTo,
                         InjectionContext injectionContext) {
        this.path = path;
        this.templateName = templateName;
        this.sourceFile = sourceFile;
        this.data = data;
        this.dataProviders = dataProviders;
        this.dataConsumers = dataConsumers;
        this.redirectTo = redirectTo;
        this.injectionContext = injectionContext;
    }

    public static TemplatedPage loadTemplatedPageEntryFrom(String templatePath, String templateName, File templateConfig, InjectionContext injectionContext) {
        String json = readFileAsString(templateConfig, "UTF-8");

        TemplateConfig page = new JsonDeserialiser(map().kind("page")
                                                           .to(TemplateConfig.class)
                                                           .build()).deserialise(json);

        try {
            return page.toEntry(templatePath, templateName, templateConfig, injectionContext);
        } catch (Exception e) {
            throw new RuntimeException(format("Could not load template [%s:%s]. (See cause).", templatePath, templateConfig), e);
        }
    }

    public TemplateContext buildTemplateContextFrom(Request request) {

        Map<String, Object> templateData = (data == null)
                ? new HashMap<String, Object>()
                : new HashMap<String, Object>(data);

        for (DataProvider provider : dataProviders) {
            provider.populateData(request, templateData);
        }

        return templateContext().fromMap(templateData);
    }


    /**
     * Note that we don't want this turned on by default! - Only for development
     * need a fancy way to do this.
     */
    public synchronized TemplatedPage autoRefresh() {
        Map<String, Object> templateData = data;
        if (sourceFile != null && sourceFile.exists()) {
            return loadTemplatedPageEntryFrom(templateName, templateName, sourceFile, injectionContext);
        }
        return this;
    }


    public void handlePOST(Request request, RedirectionParameters redirectionParameters) {
        for (DataConsumer consumer : dataConsumers) {
            consumer.consumeRequest(request, redirectionParameters);
        }
    }
}