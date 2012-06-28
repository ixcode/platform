package ixcode.platform.build.dependency;

import ixcode.platform.io.IoStreamHandling;
import ixcode.platform.io.TempFileGenerator;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import static ixcode.platform.io.IoStreamHandling.readFully;
import static ixcode.platform.io.IoStreamHandling.writeToFile;
import static ixcode.platform.io.TempFileGenerator.createNewTempFile;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DependencyRepoTest {

    @Test
    public void can_open_a_stream_to_a_file() throws Exception {

        File f = createNewTempFile("testy_stream.txt");
        f.deleteOnExit();

        writeToFile("foobar", f);

        URL url = new URL(format("file://%s", f.getAbsolutePath()));

        InputStream in = url.openStream();

        String result = readFully(in, "UTF-8");

        assertThat(result, is("foobar"));

    }


}