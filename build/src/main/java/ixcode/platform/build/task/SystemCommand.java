package ixcode.platform.build.task;

import ixcode.platform.build.BuildLog;
import ixcode.platform.build.BuildTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import static java.lang.Runtime.getRuntime;
import static java.lang.System.getenv;

public class SystemCommand implements BuildTask {

    private File dir;
    private final String command;

    private static boolean IS_CYGWIN = isCygwin();

    private static final CygwinPathFormat CYGWIN_PATH_FORMAT = new CygwinPathFormat();

    private static boolean isCygwin() {
        return "cygwin".equals(getenv("TERM"));
    }

    public SystemCommand(String command, Object... formatArgs) {
        this(expandCurrentDir(), command, formatArgs);
    }

    public static String getOsSpecificFilename(File file) {
        if (!IS_CYGWIN) {
            return file.getAbsolutePath();
        }
        try {
            String fileName = file.getCanonicalFile().getAbsolutePath();
            return CYGWIN_PATH_FORMAT.format(fileName);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static File expandCurrentDir() {
        try {
            return new File(".").getCanonicalFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SystemCommand(File dir, String command, Object... formatArgs) {
        this.dir = dir;
        this.command = String.format(command, formatArgs);
    }

    public void execute(BuildLog buildLog) {
        buildLog.println("Executing system command [%s] in dir [%s]", command, dir.getAbsolutePath());

        BufferedReader reader = null;
        Process p = null;

        try {
            p = getRuntime().exec(command, new String[0], dir);
            p.waitFor();
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                buildLog.printlndirect(line);
                line = reader.readLine();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            tryToClose(reader);
            tryToDestroy(p);
        }
    }

    private static final void tryToClose(Reader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final void tryToDestroy(Process process) {

        if (process != null) {
            process.destroy();
        }

    }
}