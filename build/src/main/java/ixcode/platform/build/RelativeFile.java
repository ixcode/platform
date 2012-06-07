package ixcode.platform.build;

import java.io.File;

public class RelativeFile {

    private final File rootDir;
    private final File dir;

    private final String relativePath;

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
}