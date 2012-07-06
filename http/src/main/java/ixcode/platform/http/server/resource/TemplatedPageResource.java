package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.protocol.response.ResponseBuilder;
import ixcode.platform.http.template.Template;
import ixcode.platform.http.template.TemplateContext;
import ixcode.platform.http.template.TemplateEngine;
import ixcode.platform.text.format.UriFormat;

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
    private String redirectTo;

    public TemplatedPageResource(TemplateEngine templateEngine,
                                 File configurationFile,
                                 String templateName,
                                 Map<String, Object> data,
                                 List<DataProvider> dataProviders,
                                 List<DataConsumer> dataConsumers,
                                 String redirectTo) {

        this.configurationFile = configurationFile;
        this.templateName = templateName;
        this.templateEngine = templateEngine;
        this.data = data;
        this.redirectTo = redirectTo;
    }


    @Override public void GET(Request request,
                              ResponseBuilder respondWith,
                              ResourceHyperlinkBuilder hyperlinkBuilder) {

        Map<String, Object> templateData = data;
        if (configurationFile != null && configurationFile.exists()) {
            TemplatedPageEntry templatedPageEntry = loadTemplatedPageEntryFrom(templateName, templateName, configurationFile);
            templateData = templatedPageEntry.data;
        }


        TemplateContext ctx = templateContext().fromMap(templateData);


        Template template = templateEngine.findTemplate(templateName);

        respondWith.status().ok()
                   .body(template.render(ctx));

    }


    @Override
    public void POST(Request request,
                     ResponseBuilder respondWith,
                     ResourceHyperlinkBuilder resourceHyperlinkBuilder) {

        String requestPath = request.getPath();
        String redirectPath = redirectTo;
        if (redirectPath.startsWith(".")) {
            redirectPath = requestPath.substring(0, requestPath.lastIndexOf("/")) + redirectTo.substring(1);
        }
        String urlRoot = request.getUrl().substring(0, requestPath.length());
        URI redirectUri = new UriFormat().parseString(urlRoot + redirectPath);

        respondWith.status().seeOther(redirectUri)
                   .contentType().html()
                   .body(format("<html><body>We tried to redirect you to <a href='%s'>here</a></body></html>", "" + redirectUri));


    }

}