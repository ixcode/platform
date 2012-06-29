package ixcode.platform.build.task;

import ixcode.platform.build.BuildLog;
import ixcode.platform.build.BuildTask;
import ixcode.platform.io.RelativeFile;

import java.io.File;

import static ixcode.platform.build.task.SystemCommand.getOsSpecificFilename;
import static ixcode.platform.build.task.SystemCommand.isCygwin;

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
        buildLog.println("Coying from [%s] to [%s]", fromFile.getAbsolutePath(), toFile.getAbsolutePath());

        if (!fromFile.exists()) {
            buildLog.println("Nothing to copy");
            return;
        }

        toFile.mkdirs();

        String osDirCopy = (isCygwin()) ? "/*" : "/";
        String toFileString = (fromFile.isDirectory()) ? fromFile.getAbsolutePath() + osDirCopy : fromFile.getAbsolutePath();

        new SystemCommand("cp -aRv %s %s", getOsSpecificFilename(toFileString), getOsSpecificFilename(toFile.getAbsolutePath())).execute(buildLog);
    }
}