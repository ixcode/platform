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


    }

    @Test
    public void get_parent_of_current_dir() throws Exception {
        File f = new File(".").getCanonicalFile();

        System.out.println(f.getAbsolutePath());
        System.out.println(f.getParent());

    }
}