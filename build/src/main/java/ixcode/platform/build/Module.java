package ixcode.platform.build;

import ixcode.platform.build.dependency.DependencyRepo;
import ixcode.platform.build.dependency.MavenArtifact;
import ixcode.platform.serialise.JsonDeserialiser;
import ixcode.platform.serialise.KindToClassMap;
import ixcode.platform.text.format.UriFormat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ixcode.platform.build.dependency.MavenArtifact.parseFromArray;
import static ixcode.platform.build.dependency.MavenArtifact.parseFromString;
import static ixcode.platform.io.IoStreamHandling.readFileAsString;
import static ixcode.platform.serialise.KindToClassMap.map;
import static java.nio.charset.Charset.defaultCharset;

public class Module {

    public final String name;
    public final MavenArtifact artifact;
    public final List<DependencyRepo> repositories;
    public final List<MavenArtifact> developmentDeps;
    public final List<MavenArtifact> productionDeps;

    private Module(File moduleDir) {
        name = moduleDir.getName();

        repositories = new ArrayList<DependencyRepo>();
        developmentDeps = new ArrayList<MavenArtifact>();
        productionDeps = new ArrayList<MavenArtifact>();
        artifact = parseFromString(name);
    }

    private Module(ModuleData moduleData) {
        name = moduleData.module.get(1);

        artifact = parseFromArray(moduleData.module.toArray(new String[0]));
        repositories = parseRepostiories(getDependenciesProperty(moduleData, "repositories"));
        developmentDeps = parseDependencies(getDependenciesProperty(moduleData, "development"));
        productionDeps = parseDependencies(getDependenciesProperty(moduleData, "production"));
    }

    private List<Object> getDependenciesProperty(ModuleData moduleData, String name) {
        if (!moduleData.dependencies.containsKey(name)) {
            return new ArrayList<Object>();
        }
        return (List<Object>)moduleData.dependencies.get(name);
    }

    private static List<MavenArtifact> parseDependencies(List<Object> objects) {
        List<MavenArtifact> dependencies = new ArrayList<MavenArtifact>();

        for(Object o : objects) {
            dependencies.add(parseFromString((String) o));
        }

        return dependencies;
    }

    private static List<DependencyRepo> parseRepostiories(List<Object> objects) {
        List<DependencyRepo> repositories = new ArrayList<DependencyRepo>();
        for (Object o : objects) {
            repositories.add(new DependencyRepo(new UriFormat().parseString((String)o)));
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

        private ModuleData(List<String> module) {
            this(module, new HashMap<String, Object>());
        }

        private ModuleData(List<String> module, Map<String, Object> dependencies) {
            this.module = module;
            this.dependencies = dependencies;
        }
    }




}