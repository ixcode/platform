package ixcode.platform.io;

import org.junit.Test;

import java.io.File;

import static ixcode.platform.io.RelativeFile.relativeFile;
import static java.lang.System.getProperty;
import static org.fest.assertions.Assertions.assertThat;

public class RelativeFileTest {

    @Test
    public void test_pattern_regex() {
        String out = RelativeFile.createRegex("*.java");

        assertThat(out).isEqualTo("(.*)\\.java");
    }

    @Test
    public void test_regex() {
        assertThat("some/file/path.java".matches("(.*)\\.java")).isTrue();
    }

    @Test
    public void lists_a_directory_recursively() {


        RelativeFile relativeFile = relativeFile(getProperty("user.home"), "tmp");

        File[] files = relativeFile.listAllFilesMatching("*.*");

        for (File f : files) {
            System.out.println(f.getAbsolutePath());
        }
    }

    @Test
    public void lists_all_pngs_in_a_directory_recursively() {

        RelativeFile relativeFile = relativeFile(getProperty("user.home"), "tmp");

        File[] files = relativeFile.listAllFilesMatching("*.png");

        for (File f : files) {
            System.out.println(f.getAbsolutePath());
        }
    }
}