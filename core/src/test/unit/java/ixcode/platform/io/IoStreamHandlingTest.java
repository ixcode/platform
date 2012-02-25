package ixcode.platform.io;

import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static ixcode.platform.io.IoStreamHandling.copyStream;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class IoStreamHandlingTest {
    private ByteArrayInputStream in;
    private ByteArrayOutputStream out;

    @After
    public void tearDown() throws IOException {
        if (in != null) {
            in.close();
        }
        
        if (out != null) {
            out.close();
        }
    }
    
    @Test
    public void testCopyStream() throws Exception {

        String input = "This is my message to you...";

        in = new ByteArrayInputStream(input.getBytes("UTF8"));
        out = new ByteArrayOutputStream();

        copyStream(in, out);
        
        String output = new String(out.toByteArray(), "UTF8");
        
        assertThat(output, is(input));
    }
}