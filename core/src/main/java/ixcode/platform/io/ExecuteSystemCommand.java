package ixcode.platform.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import static ixcode.platform.io.IoStreamHandling.closeQuietly;
import static java.lang.Runtime.getRuntime;

public class ExecuteSystemCommand {

    public static void executeSystemCommand(String command, File dir, OutputHandler handler) {
        BufferedReader reader = null;
        Process p = null;

        try {
            p = getRuntime().exec(command, new String[0], dir);
            p.waitFor();
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                handler.handleLine(line);
                line = reader.readLine();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(reader);
            tryToDestroy(p);
        }
    }

    private static final void tryToDestroy(Process process) {

        if (process != null) {
            process.destroy();
        }

    }

    public static interface OutputHandler {

        void handleLine(String line);
    }

}