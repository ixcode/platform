package ixcode.platform.build;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import java.util.Arrays;
import java.util.List;

import static javax.tools.ToolProvider.getSystemJavaCompiler;

public class Compilation {
    private BuildLog buildLog;
    private final RelativeFile sourceDir;
    private final RelativeFile targetClassesDir;

    public Compilation(BuildLog buildLog, RelativeFile sourceDir, RelativeFile targetClassesDir) {
        this.buildLog = buildLog;
        this.sourceDir = sourceDir;
        this.targetClassesDir = targetClassesDir;
    }

    public void execute() {
        buildLog.println("Compilation from [%s] to [%s]",
                         sourceDir.geRelativePath(),
                         targetClassesDir.geRelativePath());

        targetClassesDir.mkdirs();
        compile();
    }

    private void compile() {
        JavaCompiler compiler = getSystemJavaCompiler();

        List<String> optionList = Arrays.asList(
                "-g",
                "-Xlint",
                "-sourcepath",
                sourceDir.getAbsolutePath(),
                "-d",
                "./target/classes");

        StandardJavaFileManager sjfm = compiler.getStandardFileManager(null, null, null);

        Iterable fileObjects = sjfm.getJavaFileObjects(sourceDir.listAllFilesMatching("*.java"));

        JavaCompiler.CompilationTask task = compiler.getTask(
                null, null, null,
                optionList, null,
                fileObjects);

        boolean success = task.call();

        if (success) {
            buildLog.println("Compilation is successful");
        } else {
            buildLog.println("Compilation Failed");
        }

        try {
            sjfm.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}