package ixcode.platform.http.server.resource;

import ixcode.platform.di.InjectionContext;
import ixcode.platform.test.SystemTest;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SystemTest
public class TemplatedPageDirectoryTest {

    @Test
    public void loads_up_a_directory_of_templates() {
        InjectionContext injectionContext = new InjectionContext();
        List<TemplatedPage> pages = new TemplatedPageDirectory("./src/test/sample-pages", ".jade", injectionContext).findPages();

        assertThat(pages.size(), is(2));
        assertThat(pages.get(0).path, is("/auto-config/a-template"));
        assertThat(pages.get(0).templateName, is("auto-config/a-template"));

        assertThat(pages.get(1).templateName, is("subsection/some-page"));
        assertThat((String)pages.get(1).data.get("message"), is("This is my message to you"));
    }
}