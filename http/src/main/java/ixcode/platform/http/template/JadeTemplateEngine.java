package ixcode.platform.http.template;

import static java.lang.String.format;

public class JadeTemplateEngine implements TemplateEngine {

    private final TemplateLoader templateLoader;

    public JadeTemplateEngine(TemplateLoader templateLoader) {
        this.templateLoader = templateLoader;
    }

    @Override public Template findTemplate(String templateName) {
        return templateLoader.load(templateName);
    }
}