package ixcode.platform.http.template;

import de.neuland.jade4j.JadeConfiguration;

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
        InputStream in = aClass.getResourceAsStream(name);

        if (in == null) {
            throw new RuntimeException(format("Could not find template called [%s] on the classpath.", name));
        }

        return new BufferedReader(new InputStreamReader(in));
    }
}