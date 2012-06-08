package ixcode.platform.build;

import java.io.File;

import static ixcode.platform.build.RelativeFile.relativeFile;

public class Builder {

    private BuildLog buildLog;

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


        new Clean(buildLog, targetDir).execute();
        new Compilation(buildLog, sourceDir, targetClassesDir).execute();
        new Jar(buildLog, targetJarfile, targetClassesDir, resourcesDir).execute();
        new Copy(buildLog, targetDistDir, scriptDir, "*.*").execute();
    }

}