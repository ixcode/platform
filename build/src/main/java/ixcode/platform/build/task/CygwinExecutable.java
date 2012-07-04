package ixcode.platform.build.task;

import static java.lang.String.format;

public class CygwinExecutable {

    public static String pathToCygwinExe(String nameOfExe) {
        return format("C:\\Program Files (x86)\\Git\\bin\\%s", nameOfExe);
    }
}