package ixcode.platform.build.task;

import ixcode.platform.build.BuildLog;
import ixcode.platform.build.BuildTask;
import ixcode.platform.io.RelativeFile;

public class Jar implements BuildTask {

    private final RelativeFile jarFile;
    private final RelativeFile targetClassesDir;
    private final RelativeFile resourcesDir;

    public Jar(RelativeFile jarFile, RelativeFile targetClassesDir, RelativeFile resourcesDir) {
        this.jarFile = jarFile;
        this.targetClassesDir = targetClassesDir;
        this.resourcesDir = resourcesDir;
    }

    public void execute(BuildLog buildLog) {
        buildLog.println("Going to create jarfile [%s] from classes in [%s]", jarFile, targetClassesDir);
        jarFile.remove();
        jarFile.getParent().mkdirs();

        new SystemCommand(jarFile.getParentFile(), "jar -cf " + jarFile.getAbsolutePath() + " -C "+ targetClassesDir.getAbsolutePath() + " .").execute(buildLog);

        //new SystemCommand(jarFile.getParentFile(), "jar -tf " + jarFile.getAbsolutePath()).execute(buildLog);
    }
}