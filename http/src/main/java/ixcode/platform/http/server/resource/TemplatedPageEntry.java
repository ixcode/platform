package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.HttpMethod;
import ixcode.platform.serialise.JsonDeserialiser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ixcode.platform.io.IoStreamHandling.readFileAsString;
import static ixcode.platform.serialise.KindToClassMap.map;

public class TemplatedPageEntry {
    public final String path;
    public final String templateName;
    public final File sourceFile;

    public final HttpMethod[] methods = {HttpMethod.GET, HttpMethod.POST};


    public final Map<String, Object> data;

    public final List<DataProvider> dataProviders;
    public final List<DataConsumer> dataConsumers;

    public final String redirectTo;

    public TemplatedPageEntry(String path, String templateName) {
        this(path,
             templateName,
             (File) null,
             new HashMap<String, Object>(),
             new ArrayList<DataProvider>(),
             new ArrayList<DataConsumer>(),
             (String) null);
    }

    public TemplatedPageEntry(String path,
                              String templateName,
                              File sourceFile,
                              Map<String, Object> data,
                              List<DataProvider> dataProviders,
                              List<DataConsumer> dataConsumers,
                              String redirectTo) {
        this.path = path;
        this.templateName = templateName;
        this.sourceFile = sourceFile;
        this.data = data;
        this.dataProviders = dataProviders;
        this.dataConsumers = dataConsumers;
        this.redirectTo = redirectTo;
    }

    public static TemplatedPageEntry loadTemplatedPageEntryFrom(String templatePath, String templateName, File templateConfig) {
        String json = readFileAsString(templateConfig, "UTF-8");

        TemplateConfig page = new JsonDeserialiser(map().kind("page")
                                                           .to(TemplateConfig.class)
                                                           .build()).deserialise(json);

        return page.toEntry(templatePath, templateName, templateConfig);
    }
}