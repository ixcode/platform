package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.protocol.response.ResponseBuilder;
import ixcode.platform.http.template.Template;
import ixcode.platform.http.template.TemplateContext;
import ixcode.platform.http.template.TemplateEngine;
import ixcode.platform.text.format.UriFormat;

import java.net.URI;

import static java.lang.String.format;

public class TemplatedPageResource implements GetResource, PostResource {
    private final TemplateEngine templateEngine;
    private TemplatedPage page;

    public TemplatedPageResource(TemplateEngine templateEngine,
                                 TemplatedPage page) {

        this.templateEngine = templateEngine;
        this.page = page;
    }


    @Override public void GET(Request request,
                              ResponseBuilder respondWith,
                              ResourceHyperlinkBuilder hyperlinkBuilder) {

        page = page.autoRefresh();


        TemplateContext ctx = page.buildTemplateContextFrom(request);


        Template template = templateEngine.findTemplate(page.templateName);

        respondWith.status().ok()
                   .body(template.render(ctx));

    }


    @Override
    public void POST(Request request,
                     ResponseBuilder respondWith,
                     ResourceHyperlinkBuilder resourceHyperlinkBuilder) {

        page = page.autoRefresh();

        String requestPath = request.getPath();
        String redirectPath = page.redirectTo;
        if (redirectPath.startsWith(".")) {
            redirectPath = requestPath.substring(0, requestPath.lastIndexOf("/")) + page.redirectTo.substring(1);
        }
        String urlRoot = request.getUrl().substring(0, requestPath.length());
        URI redirectUri = new UriFormat().parseString(urlRoot + redirectPath);

        respondWith.status().seeOther(redirectUri)
                   .contentType().html()
                   .body(format("<html><body>We tried to redirect you to <a href='%s'>here</a></body></html>", "" + redirectUri));


    }

}