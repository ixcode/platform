package ixcode.platform.build.task;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CygwinPathFormatTest {

    @Test
    public void convertsWindowsPathsToCygwinPaths() {
        String windowsPath = "C:\\Users\\bobby\\work\\jobby\\foo\\bar";

        String cygwinPath = new CygwinPathFormat().format(windowsPath);

        assertThat(cygwinPath, is("/c/Users/bobby/work/jobby/foo/bar"));


    }

}