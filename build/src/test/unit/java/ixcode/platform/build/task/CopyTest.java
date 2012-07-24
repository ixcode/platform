package ixcode.platform.build.task;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.lang.Runtime.getRuntime;

public class CopyTest {


    public static void main(String[] args) throws IOException, InterruptedException {

        String copyCommand_a = "cp -aRv /Users/jim/work/code/github/platform/core/lib/production/* /Users/jim/tmp";

        String copyCommand_b = "/Users/jim/tmp/copy_file.sh /Users/jim/work/code/github/platform/core/lib/production/* /Users/jim/tmp";

        Process p = getRuntime()
                .exec(copyCommand_a);

        p.waitFor();
        System.out.println("Ok, ready to roll!");
        System.out.println("Exit status: " + p.exitValue());

        if (p.exitValue() != 0) {
            debugOutput(p.getErrorStream());
        } else {
            debugOutput(p.getInputStream());
        }
    }

    private static void debugOutput(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = reader.readLine();
        while (line != null) {
            System.out.println(line);
            line = reader.readLine();
        }


        reader.close();
    }

}