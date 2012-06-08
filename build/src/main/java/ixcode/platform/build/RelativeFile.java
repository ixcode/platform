package ixcode.platform.build;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

public class RelativeFile {

    private final File rootDir;
    private final File file;

    private final String relativePath;

    public static RelativeFile relativeFile(String rootDir, String dir) {
        return new RelativeFile(new File(rootDir), new File(dir));
    }

    public static RelativeFile relativeFile(File rootDir, String dir) {
        return new RelativeFile(rootDir, new File(dir));
    }


    public RelativeFile(File rootDir, File file) {
        this.rootDir = rootDir;
        this.file = file;
        this.relativePath = file.getAbsolutePath().substring(rootDir.getAbsolutePath().length());
    }

    public String geRelativePath() {
        return relativePath;
    }

    public void remove() {
        if (file.exists()) {
            delete(file);
        }
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
        return file.exists();
    }

    public String toString() {
        return relativePath;
    }

    public void mkdirs() {
        if (file.isDirectory()) {
            file.mkdirs();
        } else {
            file.getParentFile().mkdirs();
        }
    }

    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    public RelativeFile[] listSubdirs() {
        List<RelativeFile> subDirs = new ArrayList<RelativeFile>();

        File[] dirs = listDirectoriesIn(file);
        for (File subdir : dirs) {
            subDirs.add(new RelativeFile(rootDir, subdir));
        }

        return subDirs.toArray(new RelativeFile[0]);
    }

    public File[] listAllFilesMatching(String pattern) {
        List<File> files = new ArrayList<File>();
        listFilesTo(files, file, createRegex(pattern));
        return files.toArray(new File[0]);
    }

    private static void listFilesTo(List<File> files, File dir, String regex) {
        addLeavesTo(files, dir, regex);
        addDirsTo(files, dir, regex);
    }

    private static void addDirsTo(List<File> files, File dir, String regex) {
        File[] dirs = listDirectoriesIn(dir);

        for (File subDir : dirs) {
            listFilesTo(files, subDir, regex);
        }
    }

    private static File[] listDirectoriesIn(File dir) {
        return dir.listFiles(new FileFilter() {
                @Override public boolean accept(File pathname) {
                    return pathname.isDirectory();
                }
            });
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

    public File getParentFile() {
        return file.getParentFile();
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }


}