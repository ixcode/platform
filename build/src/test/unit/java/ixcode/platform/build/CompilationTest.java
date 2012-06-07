package ixcode.platform.build;

import org.junit.Test;

import javax.tools.JavaCompiler;

import java.io.File;

import static javax.tools.ToolProvider.getSystemJavaCompiler;

public class CompilationTest {

    @Test
    public void can_invoke_the_compiler_drektly() {
        JavaCompiler compiler = getSystemJavaCompiler();

        new File("./target/classes").mkdirs();

        int compilationResult = compiler.run(null,
                                             System.out, System.err,
                                             "-g",
                                             "-Xlint",
                                             "-Werror",
                                             "-sourcepath",
                                             "src/test/to_compile",
                                             "src/test/to_compile/SimpleClass.java",
                                             "-d",
                                             "./target/classes");
        if (compilationResult == 0) {
            System.out.println("Compilation is successful");
        } else {
            System.out.println("Compilation Failed");
        }
    }
}

