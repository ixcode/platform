package ixcode.platform.build;

import ixcode.platform.io.RelativeFile;

public class Clean implements BuildTask {

    private final RelativeFile file;

    public Clean(RelativeFile file) {
        this.file = file;
    }

    public void execute(BuildLog buildLog) {
        if (file.exists()) {
            buildLog.println("Clean [%s]", file.geRelativePath());
            file.remove();
        } else {
            buildLog.println("Directoy [%s] does not exist, no need to clean.", file);
        }
    }
}