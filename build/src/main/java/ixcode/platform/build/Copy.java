package ixcode.platform.build;

import ixcode.platform.io.RelativeFile;

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

        String toFileString = (fromFile.isDirectory()) ? fromFile.getAbsolutePath() + "/" : fromFile.getAbsolutePath();

        new SystemCommand("cp -aRv %s %s", toFileString, toFile.getAbsolutePath()).execute(buildLog);
    }
}