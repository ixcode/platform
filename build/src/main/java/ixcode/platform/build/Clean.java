package ixcode.platform.build;

public class Clean {
    private final BuildLog buildLog;
    private final RelativeFile file;

    public Clean(BuildLog buildLog, RelativeFile file) {
        this.buildLog = buildLog;
        this.file = file;
    }

    public void execute() {
        if (file.exists()) {
            buildLog.println("Clean [%s]", file.geRelativePath());
            file.remove();
        } else {
            buildLog.println("Directoy [%s] does not exist, no need to clean.", file);
        }
    }
}