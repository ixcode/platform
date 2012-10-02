package ixcode.platform.xml;

import org.junit.*;

import static org.fest.assertions.Assertions.assertThat;

public class XmlStringBuilderTest {

    @Test
    public void can_add_a_node_with_attributes() {
        XmlStringBuilder xb = new XmlStringBuilder();

        xb.node("foo")
             .attribute("bar", "foobar")
             .attribute("fee", "fofum")
          .closeNode("foo");

        assertThat(xb.toString()).isEqualTo("<foo bar=\"foobar\" fee=\"fofum\"></foo>");
    }

}