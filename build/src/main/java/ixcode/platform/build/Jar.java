package ixcode.platform.build;

public class Jar {
    private final BuildLog buildLog;
    private final RelativeFile jarFile;
    private final RelativeFile targetClassesDir;
    private final RelativeFile resourcesDir;

    public Jar(BuildLog buildLog, RelativeFile jarFile, RelativeFile targetClassesDir, RelativeFile resourcesDir) {
        this.buildLog = buildLog;
        this.jarFile = jarFile;
        this.targetClassesDir = targetClassesDir;
        this.resourcesDir = resourcesDir;
    }

    public void execute() {
        buildLog.println("Going to create jarfile [%s] from classes in [%s]", jarFile, targetClassesDir);
        jarFile.remove();

        new SystemCommand(buildLog, jarFile.getParentFile(), "jar -cf " + jarFile.getAbsolutePath() + " -C "+ targetClassesDir.getAbsolutePath() + " .").execute();

        new SystemCommand(buildLog, jarFile.getParentFile(), "jar -tf " + jarFile.getAbsolutePath()).execute();
    }
}