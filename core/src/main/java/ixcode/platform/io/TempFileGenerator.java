package ixcode.platform.io;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

import static java.lang.String.format;
import static org.apache.log4j.Logger.getLogger;

public class TempFileGenerator {

    private static final Logger log = getLogger(TempFileGenerator.class);

    public static File createNewTempFile(String name) {
        String tempDir = System.getProperty("java.io.tmpdir");

        String tempFileName = (name.startsWith("/") ? name : format("/%s", name));

        File f = new File(format("%s%s", tempDir, tempFileName));

        if (!f.getParentFile().exists()) {
            boolean mkdirs = f.getParentFile().mkdirs();

            if (!mkdirs) {
                throw new RuntimeException(format("Could not execute mkdirs for dir [%s]", f.getParentFile().getAbsolutePath()));
            }
        }

        try {
            f.createNewFile();
            if (!f.exists()) {
                throw new RuntimeException(format("Failed to create temp file [%s]", f.getAbsolutePath()));
            }
            log.debug(format("Created temp file [%s]", f.getAbsolutePath()));
            return f;
        } catch (IOException e) {
            throw new RuntimeException(format("Could not create temp file [%s]. (See Cause)", f.getAbsolutePath()), e);
        }


    }

}