package ixcode.platform.xml;

import org.junit.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class XmlStringBuilderTest {

    @Test
    public void can_add_a_node_with_attributes() {
        XmlStringBuilder xb = new XmlStringBuilder();

        xb.node("foo")
             .attribute("bar", "foobar")
             .attribute("fee", "fofum")
          .closeNode("foo");

        assertThat(xb.toString(), is("<foo bar=\"foobar\" fee=\"fofum\"></foo>"));
    }

}