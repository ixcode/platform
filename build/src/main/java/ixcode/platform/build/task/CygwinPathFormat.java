package ixcode.platform.build.task;

import static java.lang.String.format;

public class CygwinPathFormat {
    public String format(String fileName) {
        String withSlashesFlipped = fileName.replaceAll("\\\\", "/");

        String[] parts = withSlashesFlipped.split(":");

        String drive = String.format("/%s", parts[0].toLowerCase());


        return String.format("%s%s", drive, parts[1]);
    }
}