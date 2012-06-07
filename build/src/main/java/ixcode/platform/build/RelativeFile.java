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
}