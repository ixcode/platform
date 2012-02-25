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
    public void copies_simple_string_stream() throws Exception {

        String input = "This is my message to you...";

        in = new ByteArrayInputStream(input.getBytes("UTF8"));
        out = new ByteArrayOutputStream();

        copyStream(in, out);
        
        String output = new String(out.toByteArray(), "UTF8");
        
        assertThat(output, is(input));
    }

    @Test
    public void copies_empty_stream() throws Exception {

        String input = "";

        in = new ByteArrayInputStream(input.getBytes("UTF8"));
        out = new ByteArrayOutputStream();

        copyStream(in, out);

        String output = new String(out.toByteArray(), "UTF8");

        assertThat(output, is(input));
    }
    
    @Test
    public void conditionals_based_on_return_values() {
        int storage = 0;
        int result = (storage = iAddTwoNumbers(2, 4));
        
        assertThat(storage, is(6));
        assertThat(result, is(6));
    }
    
    private int iAddTwoNumbers(int a, int b) {
        return a + b;
    }
}