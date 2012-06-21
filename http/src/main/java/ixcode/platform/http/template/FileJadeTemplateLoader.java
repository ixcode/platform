package ixcode.platform.http.template;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.template.FileTemplateLoader;

import java.io.IOException;

public class FileJadeTemplateLoader implements TemplateLoader {

    private final String rootPath;

    public FileJadeTemplateLoader(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override public Template load(String templateName) {

        JadeConfiguration config = new JadeConfiguration();
        config.setTemplateLoader(new FileTemplateLoader(rootPath + "/", "UTF-8"));

        try {
            de.neuland.jade4j.template.JadeTemplate template = config.getTemplate(templateName);

            return new JadeTemplate(template, config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}