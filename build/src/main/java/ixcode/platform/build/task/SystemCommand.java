package ixcode.platform.build.task;

import ixcode.platform.build.BuildLog;
import ixcode.platform.build.BuildTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static java.lang.Runtime.getRuntime;
import static java.lang.System.getenv;

public class SystemCommand implements BuildTask {

    private File dir;
    private final String command;

    private static boolean IS_CYGWIN = isCygwin();

    private static final CygwinPathFormat CYGWIN_PATH_FORMAT = new CygwinPathFormat();

    public static boolean isCygwin() {
        return "cygwin".equals(getenv("TERM"));
    }

    public SystemCommand(String command, Object... formatArgs) {
        this(expandCurrentDir(), command, formatArgs);
    }

    public SystemCommand(File dir, String command, Object... formatArgs) {
        this.dir = dir;
        this.command = String.format(command, formatArgs);
    }

    private static File expandCurrentDir() {
        try {
            return new File(new File(".").getCanonicalFile().getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getOsSpecificFilename(String absolutePath) {
        if (!IS_CYGWIN) {
            return absolutePath;
        }

        return CYGWIN_PATH_FORMAT.format(absolutePath);

    }

    public void execute(BuildLog buildLog) {
        buildLog.println("Executing system command [%s] in dir [%s]", command, dir);


        Process p = null;
        try {
            p = getRuntime().exec(command, new String[0], dir);
            p.waitFor();

            printProgramOutput(buildLog, p.getInputStream(), "");
            printProgramOutput(buildLog, p.getErrorStream(), "");

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            tryToDestroy(p);
        }
    }

    private void printProgramOutput(BuildLog buildLog, InputStream in, String prefix) {
        try {
            if (in.available() <= 0) {
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            while (line != null) {
                buildLog.printlndirect(prefix + " " + line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            tryToClose(reader);
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