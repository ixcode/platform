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

    private FileToTemplatePath fileToTemplatePath;

    public TemplatedPageDirectory(String rootPath,
                                  String templateExtension,
                                  InjectionContext injectionContext) {
        this.rootPath = rootPath;
        this.templateExtension = templateExtension;
        this.injectionContext = injectionContext;

        fileToTemplatePath = new FileToTemplatePath(rootPath, templateExtension);
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
        String childPath = child.getPath();

        String templatePath = fileToTemplatePath.pathFrom(childPath);
        String templateName = templatePath.substring(1);

        String templateFilePath = childPath.substring(0, childPath.length() - templateExtension.length());

        File templateConfig = new File(templateFilePath + ".json");

        if (!templateConfig.exists()) {
            return new TemplatedPage(templatePath, templateName, injectionContext);
        }

        return loadTemplatedPageEntryFrom(templatePath, templateName, templateConfig, injectionContext);
    }

}