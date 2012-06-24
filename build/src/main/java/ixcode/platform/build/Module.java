package ixcode.platform.build;

import ixcode.platform.serialise.JsonDeserialiser;
import ixcode.platform.serialise.KindToClassMap;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ixcode.platform.io.IoStreamHandling.readFileAsString;
import static ixcode.platform.serialise.KindToClassMap.map;
import static java.nio.charset.Charset.defaultCharset;

public class Module {

    public final String name;
    public final List<DependencyRepo> repositories;
    public final List<Dependency> developmentDeps;
    public final List<Dependency> productionDeps;

    private Module(File moduleDir) {
        name = moduleDir.getName();
        repositories = new ArrayList<DependencyRepo>();
        developmentDeps = new ArrayList<Dependency>();
        productionDeps = new ArrayList<Dependency>();
    }

    private Module(ModuleData moduleData) {
        name = moduleData.module.get(1);
        repositories = parseRepostiories((List<Object>)moduleData.dependencies.get("repositories"));
        developmentDeps = parseDependencies((List<Object>)moduleData.dependencies.get("development"));
        productionDeps = parseDependencies((List<Object>)moduleData.dependencies.get("production"));
    }

    private static List<Dependency> parseDependencies(List<Object> objects) {
        List<Dependency> dependencies = new ArrayList<Dependency>();

        for(Object o : objects) {
            dependencies.add(Dependency.parseFromString((String)o));
        }

        return dependencies;
    }

    private static List<DependencyRepo> parseRepostiories(List<Object> objects) {
        List<DependencyRepo> repositories = new ArrayList<DependencyRepo>();
        for (Object o : objects) {
            repositories.add(new DependencyRepo((String)o));
        }
        return repositories;
    }

    public static  Module loadModule(File moduleDir, BuildLog buildLog) {
        File configFile = new File(moduleDir.getAbsolutePath() + "/module.ibx");

        if (!configFile.exists()) {
            return new Module(moduleDir);
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
        public final Map<String, Object> dependencies;

        private ModuleData(List<String> module, Map<String, Object> dependencies) {
            this.module = module;
            this.dependencies = dependencies;
        }
    }




}