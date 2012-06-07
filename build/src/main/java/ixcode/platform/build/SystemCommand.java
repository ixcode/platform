package ixcode.platform.build;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import static java.lang.Runtime.getRuntime;

public class SystemCommand {

    private final BuildLog buildLog;
    private File dir;
    private final String command;

    public SystemCommand(BuildLog buildLog, File dir, String command) {
        this.buildLog = buildLog;
        this.dir = dir;
        this.command = command;
    }

    public void execute() {
        buildLog.println("Executing system command [%s]", command);

        BufferedReader reader = null;
        Process p = null;

        try {
            p = getRuntime().exec(command, new String[0], dir);
            p.waitFor();
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                buildLog.println(line);
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