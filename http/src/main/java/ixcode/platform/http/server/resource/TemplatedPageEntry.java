package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.HttpMethod;
import ixcode.platform.serialise.JsonDeserialiser;

import java.io.File;
import java.net.URI;
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

    public final String redirectTo;
    public final List<DataProvider> dataProviders = new ArrayList<DataProvider>();
    public final List<DataConsumer> dataConsumers = new ArrayList<DataConsumer>();

    public TemplatedPageEntry(String path, String templateName) {
        this(path, templateName, null, new HashMap<String, Object>(), null);
    }

    public TemplatedPageEntry(String path,
                              String templateName,
                              File sourceFile,
                              Map<String, Object> data,
                              String redirectTo) {
        this.path = path;
        this.templateName = templateName;
        this.sourceFile = sourceFile;
        this.data = data;
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