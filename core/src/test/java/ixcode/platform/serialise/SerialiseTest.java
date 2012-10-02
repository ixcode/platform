package ixcode.platform.serialise;

import org.junit.*;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

public class SerialiseTest {

    @Mock private XmlSerialiser xmlSerialiser;
    @Mock private JsonSerialiser jsonSerialiser;

    private Serialise serialise;

    @Before
    public void setup() {
        initMocks(this);
        serialise = new Serialise(xmlSerialiser, jsonSerialiser);
    }

    @Test
    public void uses_xml_serialiser() {
        when(xmlSerialiser.toXml("SomeObject")).thenReturn("foo");

        String xml = serialise.toXml("SomeObject");

        assertThat(xml).isEqualTo("foo");
    }

    @Test
    public void uses_json_serialiser() {
        when(jsonSerialiser.toJson("SomeObject")).thenReturn("bar");

        String xml = serialise.toJson("SomeObject");

        assertThat(xml).isEqualTo("bar");
    }

}