package ixcode.platform.build.task;

import ixcode.platform.build.BuildLog;
import ixcode.platform.build.BuildTask;
import ixcode.platform.io.RelativeFile;

import static ixcode.platform.build.task.CygwinExecutable.pathToCygwinExe;
import static ixcode.platform.build.task.SystemCommand.getOsSpecificFilename;
import static ixcode.platform.build.task.SystemCommand.isCygwin;

public class Tar implements BuildTask {
    private final RelativeFile tarFile;
    private final RelativeFile dir;

    public Tar(RelativeFile tarFile, RelativeFile dir) {
        this.tarFile = tarFile;
        this.dir = dir;
    }

    @Override public void execute(BuildLog buildLog) {
        buildLog.println("tar'ing up [%s] to [%s]", dir, tarFile);

        String tarCommand = (isCygwin()) ? pathToCygwinExe("tar.exe") : "tar";

        new SystemCommand(dir.asFile(), "%s cfzv %s . -i *",tarCommand,
                          getOsSpecificFilename(tarFile.getAbsolutePath())).execute(buildLog);
    }
}