package ixcode.platform.build;

public class CopyResources implements BuildTask {
    private final RelativeFile resourceDir;
    private final RelativeFile targetClassesDir;

    public CopyResources(RelativeFile resourceDir, RelativeFile targetClassesDir) {
        this.resourceDir = resourceDir;
        this.targetClassesDir = targetClassesDir;
    }

    @Override public void execute(BuildLog buildLog) {
        buildLog.println("Copying resources from [%s] to [%s]", resourceDir, targetClassesDir);

        targetClassesDir.mkdirs();

        RelativeFile[] subdirs = resourceDir.listSubdirs();

        for (RelativeFile subdir : subdirs) {
            new Copy(subdir, targetClassesDir).execute(buildLog);
        }
    }
}