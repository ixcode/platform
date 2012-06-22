package ixcode.platform.build;

import org.junit.Test;

import static java.lang.System.out;
import static java.lang.System.setOut;
import static java.lang.Thread.sleep;

public class OutputStatusTest {

    @Test
    public void display_status() throws Exception {

        int delayMs = 500;

        System.out.println("Going to download some jars...");
        System.out.println("               0                 100%");
        System.out.print("               |--------------------|\nlog4j:1.03     |");
        for (int i = 0; i<10;++i) {
            out.print("**");
            sleep(delayMs);
        }
        System.out.print("|\n               |--------------------|");
        System.out.println("\nComplete.");

    }

}