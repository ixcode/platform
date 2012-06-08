package ixcode.platform.build;

import java.io.File;

import static ixcode.platform.build.RelativeFile.relativeFile;

public class Builder {

    private final BuildLog buildLog;

    public static void main(String[] args) {
        new Builder(new ConsoleLog()).buildModule(new File(args[0]));
    }

    public Builder(BuildLog consoleLog) {
        this.buildLog = consoleLog;
    }


    public void buildModule(File moduleDir) {
        buildLog.println("Builder (v.10) - building now!");
        buildLog.println("Module Dir [%s]", moduleDir);

        RelativeFile sourceDir = relativeFile(moduleDir, "src/main/java");
        RelativeFile resourcesDir = relativeFile(moduleDir, "src/main/resource");
        RelativeFile scriptDir = relativeFile(moduleDir, "src/main/script");


        RelativeFile targetDir = relativeFile(moduleDir, "target");
        RelativeFile targetClassesDir = relativeFile(moduleDir, "target/work/classes");
        RelativeFile targetDistDir = relativeFile(moduleDir, "target/dist");
        RelativeFile targetJarfile = relativeFile(moduleDir, "target/dist" + moduleDir.getName() + ".jar");


        new Compilation(sourceDir, targetClassesDir).execute(buildLog);
        new Jar(targetJarfile, targetClassesDir, resourcesDir).execute(buildLog);
        new Copy(targetDistDir, scriptDir, "*.*").execute(buildLog);
    }

    public void cleanModule(File moduleDir) {
        RelativeFile targetDir = relativeFile(moduleDir, "target");
        new Clean(targetDir).execute(buildLog);
    }

}