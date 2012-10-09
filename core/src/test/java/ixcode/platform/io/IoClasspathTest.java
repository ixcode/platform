package ixcode.platform.io;

import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static ixcode.platform.io.IoClasspath.inputStreamFromClasspathEntry;
import static org.fest.assertions.Assertions.assertThat;

public class IoClasspathTest {

    @Test
    @Ignore("some problem with maven")
    public void can_load_a_resource_from_classpath_by_class_location() throws IOException {

        InputStream in = inputStreamFromClasspathEntry(this, "test_file.txt");

        assertThat(in).isNotNull();

        String result = slurpInputStream(in);

        assertThat(result).isEqualTo("this is my test\nfile\n");

    }

    private static String slurpInputStream(InputStream in) throws IOException {BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        StringBuilder fileContents = new StringBuilder();
        try {
            while (reader.ready()) {
                fileContents.append(reader.readLine()).append("\n");
            }
        } finally {
            reader.close();
        }


        return fileContents.toString();
    }
}