package ixcode.platform.build.task;

import ixcode.platform.build.BuildLog;
import ixcode.platform.build.BuildTask;
import ixcode.platform.io.RelativeFile;

import java.io.File;

public class Copy implements BuildTask {
    private final File fromFile;
    private final File toFile;

    public Copy(RelativeFile fromFile, RelativeFile toFile) {
        this(fromFile.asFile(), toFile.asFile());
    }

    public Copy(File fromFile, File toFile) {
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