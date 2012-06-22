package ixcode.platform.build;

import ixcode.platform.serialise.JsonDeserialiser;
import ixcode.platform.serialise.KindToClassMap;

import java.io.File;
import java.util.List;

import static ixcode.platform.io.IoStreamHandling.readFileAsString;
import static ixcode.platform.serialise.KindToClassMap.map;
import static java.nio.charset.Charset.defaultCharset;

public class Module {

    public final String name;

    private Module(File moduleDir) {
        name = moduleDir.getName();
    }

    private Module(ModuleData moduleData) {
        name = moduleData.module.get(1);
    }

    public static  Module loadModule(File moduleDir, BuildLog buildLog) {
        File configFile = new File(moduleDir.getAbsolutePath() + "/module.ibx");

        if (!configFile.exists()) {
            return new Module(moduleDir.getParentFile());
        }

        buildLog.println("Loading configuration from [%s]", configFile.getAbsolutePath());

        KindToClassMap kindToClassMap = map()
                .kind("ibx-module").to(ModuleData.class)
                .build();

        JsonDeserialiser parser = new JsonDeserialiser(kindToClassMap);
        ModuleData moduleData =  parser.deserialise(readFileAsString(configFile, defaultCharset().name()));
        return new Module(moduleData);
    }

    private static class ModuleData {

        public final List<String> module;

        private ModuleData(List<String> module) {
            this.module = module;
        }
    }




}