package ixcode.platform.serialise;

import ixcode.platform.json.JsonObject;
import org.junit.Test;

import java.util.Map;

import static ixcode.platform.collection.MapBuilder.linkedHashMapWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TreeStyleJsonPrinterTest {

    @Test
    public void prints_some_json() {
        TreeStyleJsonPrinter printer = new TreeStyleJsonPrinter();

        Map<String, Object> valueMap = linkedHashMapWith()
                                            .key("foo").value(34)
                                            .key("bar").value("bar")
                                            .build();

        String result = printer.print(new JsonObject(valueMap)).toString();

        assertThat(result, is("{ \"foo\" : 34, \"bar\" : \"bar\" }"));
    }

}