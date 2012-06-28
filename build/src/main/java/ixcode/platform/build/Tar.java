package ixcode.platform.build;

import ixcode.platform.io.RelativeFile;

public class Tar implements BuildTask {
    private final RelativeFile tarFile;
    private final RelativeFile dir;

    public Tar(RelativeFile tarFile, RelativeFile dir) {
        this.tarFile = tarFile;
        this.dir = dir;
    }

    @Override public void execute(BuildLog buildLog) {
        buildLog.println("tar'ing up [%s] to [%s]", dir, tarFile);
        new SystemCommand(dir.asFile(), "tar cfz %s . -i *", tarFile.getAbsolutePath()).execute(buildLog);
    }
}