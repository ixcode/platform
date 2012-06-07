package ixcode.platform.build;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

public class RelativeFile {

    private final File rootDir;
    private final File dir;

    private final String relativePath;

    public static RelativeFile relativeFile(String rootDir, String dir) {
        return new RelativeFile(rootDir, dir);
    }

    public RelativeFile(String rootDir, String dir) {
        this(new File(rootDir), new File(rootDir, dir));
    }

    public RelativeFile(File rootDir, File dir) {
        this.rootDir = rootDir;
        this.dir = dir;
        this.relativePath = dir.getAbsolutePath().substring(rootDir.getAbsolutePath().length());
    }

    public String geRelativePath() {
        return relativePath;
    }

    public void remove() {
        delete(dir);
    }

    void delete(File f) {
        if (f.isDirectory()) {
            for (File child : f.listFiles()) {
                delete(child);
            }
        }
        if (!f.delete()) {
            throw new RuntimeException("Failed to delete file: " + f);
        }
    }

    public boolean exists() {
        return dir.exists();
    }

    public String toString() {
        return relativePath;
    }

    public void mkdirs() {
        dir.mkdirs();
    }

    public String getAbsolutePath() {
        return dir.getAbsolutePath();
    }

    public File[] listAllFilesMatching(String pattern) {
        List<File> files = new ArrayList<File>();
        listFilesTo(files, dir, createRegex(pattern));
        return files.toArray(new File[0]);
    }

    private static void listFilesTo(List<File> files, File dir, String regex) {
        addLeavesTo(files, dir, regex);
        addDirsTo(files, dir, regex);
    }

    private static void addDirsTo(List<File> files, File dir, String regex) {
        File[] dirs = dir.listFiles(new FileFilter() {
            @Override public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        for (File subDir : dirs) {
            listFilesTo(files, subDir, regex);
        }
    }

    private static void addLeavesTo(List<File> files, File dir, final String regex) {
        File[] leafFiles = dir.listFiles(new FileFilter() {
            @Override public boolean accept(File pathname) {
                return !pathname.isDirectory()
                        && matches(pathname.getAbsolutePath(), regex);
            }
        });
        files.addAll(asList(leafFiles));
    }

    private static boolean matches(String path, String regex) {
        return path.matches(regex);
    }

    public static String createRegex(String pattern) {
        return pattern.replace(".", "\\.").replace("*", "(.*)");
    }
}