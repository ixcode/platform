package ixcode.platform.http.template;

import org.junit.Test;

import java.io.InputStream;
import java.net.URL;

import static ixcode.platform.http.template.TemplateContext.templateContext;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class JadeTemplateEngineTest {


    @Test
    public void renders_a_jade_template() {

        TemplateContext ctx = templateContext()
                .key("pageName").value("This is my page")
                .key("message").value("Yo! I is talkin to you!");


        TemplateLoader loader = new ClasspathJadeTemplateLoader(this.getClass());

        Template t = new JadeTemplateEngine(loader).findTemplate("test.jade");

        String output = t.render(ctx);

        System.out.println("Resulting page:\n");
        System.out.println(output);

        assertThat(output, containsString("<title>This is my page</title>"));
        assertThat(output, containsString("<p>Yo! I is talkin to you!</p>"));

    }

}