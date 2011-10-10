package ixcode.platform.serialise;

import org.junit.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

public class SerialiseTest {

    private Serialise serialise;

    @Mock private XmlSerialiser xmlSerialiser;
    @Mock private JsonSerialiser jsonSerialiser;

    @Before
    public void setup() {
        initMocks(this);
        serialise = new Serialise(xmlSerialiser, jsonSerialiser);
    }

    @Test
    public void uses_xml_serialiser() {
        when(xmlSerialiser.toXml("SomeObject")).thenReturn("foo");

        String xml = serialise.toXml("SomeObject");

        assertThat(xml, is("foo"));
    }

    @Test
    public void uses_json_serialiser() {
        when(jsonSerialiser.toJson("SomeObject")).thenReturn("bar");

        String xml = serialise.toJson("SomeObject");

        assertThat(xml, is("bar"));
    }

}