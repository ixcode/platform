package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.protocol.response.ResponseBuilder;
import ixcode.platform.http.template.Template;
import ixcode.platform.http.template.TemplateContext;
import ixcode.platform.http.template.TemplateEngine;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static ixcode.platform.http.server.resource.TemplatedPageEntry.loadTemplatedPageEntryFrom;
import static ixcode.platform.http.template.TemplateContext.templateContext;
import static java.lang.String.format;

public class TemplatedPageResource implements GetResource, PostResource{
    private File configurationFile;
    private final String templateName;
    private final TemplateEngine templateEngine;
    private Map<String, Object> data;
    private URI redirectUri;

    public TemplatedPageResource(TemplateEngine templateEngine,
                                 File configurationFile,
                                 String templateName,
                                 Map<String, Object> data,
                                 List<DataProvider> dataProviders,
                                 List<DataConsumer> dataConsumers,
                                 URI redirectUri) {

        this.configurationFile = configurationFile;
        this.templateName = templateName;
        this.templateEngine = templateEngine;
        this.data = data;
        this.redirectUri = redirectUri;
    }


    @Override public void GET(Request request,
                              ResponseBuilder respondWith,
                              ResourceHyperlinkBuilder hyperlinkBuilder) {

        TemplatedPageEntry templatedPageEntry = loadTemplatedPageEntryFrom(templateName, templateName, configurationFile);


        TemplateContext ctx = templateContext().fromMap(templatedPageEntry.data);


        Template template = templateEngine.findTemplate(templateName);

        respondWith.status().ok()
                   .body(template.render(ctx));

    }


    @Override
    public void POST(Request request,
                     ResponseBuilder respondWith,
                     ResourceHyperlinkBuilder resourceHyperlinkBuilder) {


        respondWith.status().seeOther(redirectUri)
                   .contentType().html()
                   .body(format("<html><body>Answer is <a href='%s'>here</a></body></html>", "" + redirectUri));


    }

}