package ixcode.platform.build;

public class Copy implements BuildTask {
    private final RelativeFile fromFile;
    private final RelativeFile toFile;

    public Copy(RelativeFile fromFile, RelativeFile toFile) {
        this.fromFile = fromFile;
        this.toFile = toFile;
    }

    public void execute(BuildLog buildLog) {
        buildLog.println("Coying from [%s] to [%s]", fromFile, toFile);

        toFile.mkdirs();

        new SystemCommand("cp -aRv %s %s", fromFile.getAbsolutePath() + "/", toFile.getAbsolutePath()).execute(buildLog);
    }
}