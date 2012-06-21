package ixcode.platform.http.template;

import de.neuland.jade4j.JadeConfiguration;

public class JadeTemplate implements Template {
    private final de.neuland.jade4j.template.JadeTemplate template;
    private final JadeConfiguration config;

    public JadeTemplate(de.neuland.jade4j.template.JadeTemplate template,
                        JadeConfiguration config) {
        this.template = template;
        this.config = config;
    }

    @Override public String render(TemplateContext ctx) {
        return config.renderTemplate(template, ctx.asMap());
    }
}