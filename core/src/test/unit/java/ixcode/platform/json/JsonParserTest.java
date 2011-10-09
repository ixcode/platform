package ixcode.platform.json;

import org.junit.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class JsonParserTest {

    @Test
    public void named_array() {
        String json = "{ \"anArray\" : [ { \"child\" : \"foo\" } ] }";

        JsonObject rootObject = new JsonParser().parse(json);

        JsonArray jsonArray = rootObject.valueOf("anArray");

        assertThat(jsonArray.size(), is(1));

        JsonObject childObject = jsonArray.valueAt(0);

        assertThat(((JsonString)childObject.valueOf("child")).value, is("foo"));

    }

    @Test
    public void array_of_objects() {
        String json = "[ { \"child\" : \"foo\" } ]";

        JsonArray jsonArray = new JsonParser().parse(json);

        assertThat(jsonArray.size(), is(1));

        JsonObject childObject = jsonArray.valueAt(0);

        assertThat(((JsonString)childObject.valueOf("child")).value, is("foo"));
    }
}