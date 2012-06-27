package ixcode.platform.build;

import org.junit.Test;

import static ixcode.platform.build.Dependency.parseFromString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DependencyTest {

    @Test
    public void knows_how_to_make_its_jar_filename() {
        Dependency d = parseFromString("org.apache.ant:ant:1.8.1");

        assertThat(d.toJarFileName(), is("ant-1.8.1.jar"));

    }

    @Test
    public void knows_how_to_make_maven_path() {

        Dependency d = parseFromString("org.apache.ant:ant:1.8.1");


        assertThat(d.toMavenRepositoryPath(), is("org/apache/ant/ant/1.8.1/ant-1.8.1.jar"));

    }

}