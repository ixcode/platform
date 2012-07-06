package ixcode.platform.http.server.resource;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

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
        String path = getPathFrom(child);
        return new TemplatedPageEntry(path, path.substring(1));
    }

    private String getPathFrom(File child) {
        String path = child.getPath();
        String pathWithoutRoot = path.substring(rootPath.length());
        return pathWithoutRoot.substring(0, pathWithoutRoot.length() - templateExtension.length());
    }
}