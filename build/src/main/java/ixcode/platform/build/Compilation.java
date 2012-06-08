package ixcode.platform.build;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.setProperty;
import static java.util.Arrays.asList;
import static javax.tools.ToolProvider.getSystemJavaCompiler;

public class Compilation implements BuildTask {

    private final RelativeFile sourceDir;
    private final RelativeFile productionLibDir;
    private final RelativeFile targetClassesDir;

    public Compilation(RelativeFile sourceDir,
                       RelativeFile productionLibDir,
                       RelativeFile targetClassesDir) {
        this.sourceDir = sourceDir; this.productionLibDir = productionLibDir;
        this.targetClassesDir = targetClassesDir;
    }

    public void execute(BuildLog buildLog) {
        buildLog.println("Compilation from [%s] to [%s] (libs in [%s]",
                         sourceDir.geRelativePath(),
                         targetClassesDir.geRelativePath(),
                         productionLibDir);

        targetClassesDir.mkdirs();
        compile(buildLog);
    }

    private void compile(BuildLog buildLog) {
        String oldUserDir = System.getProperty("user.dir");

        try {
            setProperty("user.dir", productionLibDir.getRoot().getAbsolutePath());

            JavaCompiler compiler = getSystemJavaCompiler();

            List<String> optionList = asList(
                    "-g",
                    "-verbose",
                    "-Xlint",
                    "-cp",
                    classpathFrom(productionLibDir),
                    "-sourcepath",
                    sourceDir.getAbsolutePath(),
                    "-d",
                    targetClassesDir.getAbsolutePath());


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
                throw new RuntimeException("Compilation failed");
            }

            try {
                sjfm.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } finally {
            setProperty("user.dir", oldUserDir);
        }

    }

    private static String classpathFrom(RelativeFile file) {
        File[] jarfiles = file.listAllFilesMatching("*.jar");

        StringBuilder sb = new StringBuilder();

        for (File jarfile : jarfiles) {
            sb.append(jarfile.getAbsolutePath());
            if (jarfile != jarfiles[jarfiles.length - 1]) {
                sb.append(":");
            }
        }

        return sb.toString();
    }
}