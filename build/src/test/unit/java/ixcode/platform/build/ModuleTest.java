package ixcode.platform.build;

import org.junit.Test;

import java.io.File;

import static ixcode.platform.build.Module.loadModule;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ModuleTest {



    @Test
    public void can_load_from_a_file() throws Exception {
        File moduleDir = new File(".").getCanonicalFile();

        Module module = loadModule(moduleDir, new ConsoleLog());

        assertThat(module.name, is("platform-build"));
        assertThat(module.repositories.size(), is(1));
        assertThat(module.repositories.get(0).location, is("http://platform.ixcode.org/artifacts"));

        assertThat(module.developmentDeps.size(), is(1));
        assertThat(module.developmentDeps.get(0).toString(), is("hamcrest:null:null"));

        assertThat(module.productionDeps.size(), is(2));
        assertThat(module.productionDeps.get(0).toString(), is("log4j:null:null"));
        assertThat(module.productionDeps.get(1).toString(), is("org.ixcode:platform-core:1.0"));

    }

    @Test
    public void get_parent_of_current_dir() throws Exception {
        File f = new File(".").getCanonicalFile();

        System.out.println(f.getAbsolutePath());
        System.out.println(f.getParent());

    }
}