package ixcode.platform.build.dependency;

import ixcode.platform.build.dependency.MavenArtifact;
import org.junit.Test;

import static ixcode.platform.build.dependency.MavenArtifact.parseFromString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MavenArtifactTest {

    @Test
    public void knows_how_to_make_its_jar_filename() {
        MavenArtifact d = parseFromString("org.apache.ant:ant:1.8.1");

        assertThat(d.toJarFileName(), is("ant-1.8.1.jar"));

    }

    @Test
    public void knows_how_to_make_maven_path() {

        MavenArtifact d = parseFromString("org.apache.ant:ant:1.8.1");


        assertThat(d.toMavenRepositoryPath(), is("org/apache/ant/ant/1.8.1/ant-1.8.1.jar"));

    }

}