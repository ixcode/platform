package ixcode.platform.http.template;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.filter.MarkdownFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static java.lang.String.format;

public class ClasspathJadeTemplateLoader implements TemplateLoader, de.neuland.jade4j.template.TemplateLoader {

    private Class<?> aClass;

    public ClasspathJadeTemplateLoader(Class<?> aClass) {
        this.aClass = aClass;
    }

    @Override public Template load(String templateName) {

        JadeConfiguration config = new JadeConfiguration();
        config.setTemplateLoader(this);
        config.setFilter("markdown", new MarkdownFilter());

        try {
            de.neuland.jade4j.template.JadeTemplate template = config.getTemplate(templateName);

            return new JadeTemplate(template, config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override public long getLastModified(String name) throws IOException {
        return 0;
    }

    @Override public Reader getReader(String name) throws IOException {
        String templateName = format("%s.jade", name);
        InputStream in = aClass.getResourceAsStream(templateName);

        if (in == null) {
            throw new RuntimeException(format("Could not find template called [%s] on the classpath.", templateName));
        }

        return new BufferedReader(new InputStreamReader(in));
    }
}