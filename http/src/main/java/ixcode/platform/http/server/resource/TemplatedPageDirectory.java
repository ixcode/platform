package ixcode.platform.http.server.resource;

import ixcode.platform.di.InjectionContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static ixcode.platform.http.server.resource.TemplatedPage.loadTemplatedPageEntryFrom;

public class TemplatedPageDirectory {
    private final String rootPath;
    private final String templateExtension;
    private InjectionContext injectionContext;

    public TemplatedPageDirectory(String rootPath,
                                  String templateExtension,
                                  InjectionContext injectionContext) {
        this.rootPath = rootPath;
        this.templateExtension = templateExtension;
        this.injectionContext = injectionContext;
    }

    public List<TemplatedPage> findPages() {
        List<TemplatedPage> pages = new ArrayList<TemplatedPage>();

        File dir = new File(rootPath);

        loadDir(pages, dir);

        return pages;
    }

    private void loadDir(List<TemplatedPage> pages, File dir) {
        File[] children = dir.listFiles();

        for (File child : children) {
            if (child.isDirectory()) {
                loadDir(pages, child);
            } else if (child.getName().endsWith(templateExtension)) {
                pages.add(loadEntryFrom(child));
            }
        }
    }

    private TemplatedPage loadEntryFrom(File child) {
        String templatePath = getPathFrom(child);
        String templateName = templatePath.substring(1);

        String templateFilePath = child.getPath().substring(0, child.getPath().length() - templateExtension.length());

        File templateConfig = new File(templateFilePath + ".json");

        if (!templateConfig.exists()) {
            return new TemplatedPage(templatePath, templateName, injectionContext);
        }

        return loadTemplatedPageEntryFrom(templatePath, templateName, templateConfig, injectionContext);
    }

    private String getPathFrom(File child) {
        String path = child.getPath();
        String pathWithoutRoot = path.substring(rootPath.length());
        return pathWithoutRoot.substring(0, pathWithoutRoot.length() - templateExtension.length());
    }

}