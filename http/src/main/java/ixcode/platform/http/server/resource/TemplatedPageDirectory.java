package ixcode.platform.http.server.resource;

import ixcode.platform.serialise.JsonDeserialiser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ixcode.platform.io.IoStreamHandling.readFileAsString;
import static ixcode.platform.serialise.KindToClassMap.map;

public class TemplatedPageDirectory {
    private final String rootPath;
    private final String templateExtension;

    public TemplatedPageDirectory(String rootPath, String templateExtension) {
        this.rootPath = rootPath;
        this.templateExtension = templateExtension;
    }

    public List<TemplatedPageEntry> findPages() {
        List<TemplatedPageEntry> pages = new ArrayList<TemplatedPageEntry>();

        File dir = new File(rootPath);

        loadDir(pages, dir);

        return pages;
    }

    private void loadDir(List<TemplatedPageEntry> pages, File dir) {
        File[] children = dir.listFiles();

        for (File child : children) {
            if (child.isDirectory()) {
                loadDir(pages, child);
            } else if (child.getName().endsWith(templateExtension)) {
                pages.add(loadEntryFrom(child));
            }
        }
    }

    private TemplatedPageEntry loadEntryFrom(File child) {
        String templatePath = getPathFrom(child);
        String templateName = templatePath.substring(1);

        String templateFilePath = child.getPath().substring(0, child.getPath().length() - templateExtension.length());

        File templateConfig = new File(templateFilePath + ".json");

        if (!templateConfig.exists()) {
            return new TemplatedPageEntry(templatePath, templateName);
        }

        String json = readFileAsString(templateConfig, "UTF-8");

        TemplateConfig page = new JsonDeserialiser(map().kind("page")
                                                           .to(TemplateConfig.class)
                                                           .build()).deserialise(json);

        return page.toEntry(templatePath, templateName);
    }

    private String getPathFrom(File child) {
        String path = child.getPath();
        String pathWithoutRoot = path.substring(rootPath.length());
        return pathWithoutRoot.substring(0, pathWithoutRoot.length() - templateExtension.length());
    }

    private static class TemplateConfig {
        public final Map<String, Object> get;
        public final Map<String, Object> post;

        private TemplateConfig(Map<String, Object> get,
                               Map<String, Object> post) {
            this.get = get;
            this.post = post;
        }

        public TemplatedPageEntry toEntry(String path, String templateName) {
            return new TemplatedPageEntry(path, templateName, (Map<String, Object>)get.get("data"));
        }
    }
}