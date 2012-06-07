package ixcode.platform.build;

import static java.lang.String.format;
import static java.lang.System.out;

public class Compilation {
    private BuildLog buildLog;
    private final RelativeFile sourceDir;
    private final RelativeFile targetClassesDir;

    public Compilation(BuildLog buildLog, RelativeFile sourceDir, RelativeFile targetClassesDir) {
        this.buildLog = buildLog;
        this.sourceDir = sourceDir;
        this.targetClassesDir = targetClassesDir;
    }

    public void execute() {
        buildLog.println("Compilation from [%s] to [%s]",
                         sourceDir.geRelativePath(),
                         targetClassesDir.geRelativePath());

        targetClassesDir.mkdirs();
    }
}