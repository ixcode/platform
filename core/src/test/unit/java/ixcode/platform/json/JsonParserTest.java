package ixcode.platform.json;

import org.junit.*;

import static ixcode.platform.json.JsonString.jsonString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class JsonParserTest {

    @Test
    public void object_containing_an_array() {
        String json = "{ \"anArray\" : [ { \"child\" : \"foo\" } ] }";

        JsonObject rootObject = new JsonParser().parse(json);

        JsonArray jsonArray = rootObject.valueOf("anArray");

        assertThat(jsonArray.size(), is(1));

        JsonObject childObject = jsonArray.valueAt(0);

        assertThat(childObject.<JsonString>valueOf("child"), is(jsonString("foo")));

    }

    @Test
    public void array_of_objects() {
        String json = "[ { \"child\" : \"foo\" } ]";

        JsonArray jsonArray = new JsonParser().parse(json);

        assertThat(jsonArray.size(), is(1));

        JsonObject childObject = jsonArray.valueAt(0);

        assertThat(childObject.<JsonString>valueOf("child"), is(jsonString("foo")));
    }
}