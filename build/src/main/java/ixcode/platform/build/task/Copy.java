package ixcode.platform.build.task;

import ixcode.platform.build.BuildLog;
import ixcode.platform.build.BuildTask;
import ixcode.platform.io.RelativeFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import static ixcode.platform.build.task.CygwinExecutable.pathToCygwinExe;
import static ixcode.platform.build.task.SystemCommand.getOsSpecificFilename;
import static ixcode.platform.build.task.SystemCommand.isCygwin;
import static ixcode.platform.io.IoStreamHandling.closeQuietly;
import static java.lang.System.getProperty;

public class Copy implements BuildTask {
    private final File fromFile;
    private final File toFile;

    private static String COPY_SCRIPT = createCopyScript();

    /**
     * AAAAARRRRGH!! give me jdk 7 on the mac, pleeeese
     */
    private static String createCopyScript() {
        if (isCygwin()) {
            return "NOT AVAILABLE IN CYGWIN!!";
        }

        String copy2FilesScript = "#!/bin/sh\n" + "cp -aRv $1 $2\n";
        File f = new File(getProperty("user.home") + "/copy_file.sh");

        if (f.exists()) {
            return f.getAbsolutePath();
        }

        BufferedWriter out = null;
        try {
            f.createNewFile();
            out = new BufferedWriter(new FileWriter(f));

            out.write(copy2FilesScript);
            System.out.println("Written to file " + f.getAbsolutePath());

            Runtime.getRuntime().exec("chmod +x " + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(out);
        }
    }

    public Copy(RelativeFile fromFile, RelativeFile toFile) {
        this(fromFile.asFile(), toFile.asFile());
    }

    public Copy(File fromFile, File toFile) {
        this.fromFile = fromFile;
        this.toFile = toFile;
    }

    public void execute(BuildLog buildLog) {
        buildLog.println("Copying from [%s] to [%s]", fromFile.getAbsolutePath(), toFile.getAbsolutePath());

        if (!fromFile.exists()) {
            buildLog.println("Nothing to copy");
            return;
        }

        toFile.mkdirs();


        String osDirFromCopy = (isCygwin()) ? "\\*" : "/";
        String osDirToCopy = (isCygwin()) ? "\\" : "";

        String fromFileString = (fromFile.isDirectory()) ? fromFile.getAbsolutePath() + osDirFromCopy : fromFile.getAbsolutePath();
        String toFileString = (toFile.isDirectory()) ? toFile.getAbsolutePath() + osDirToCopy : toFile.getAbsolutePath();

        String copyCommand = (isCygwin()) ? pathToCygwinExe("cp.exe  -aRv") : COPY_SCRIPT;

        new SystemCommand("%s %s %s", copyCommand, getOsSpecificFilename(fromFileString), getOsSpecificFilename(toFileString)).execute(buildLog);
    }
}