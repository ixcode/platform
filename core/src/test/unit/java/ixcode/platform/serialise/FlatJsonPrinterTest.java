package ixcode.platform.serialise;

import ixcode.platform.json.JsonArray;
import ixcode.platform.json.JsonObject;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static ixcode.platform.collection.MapBuilder.linkedHashMapWith;
import static ixcode.platform.json.JsonArrayBuilder.jsonArrayWith;
import static ixcode.platform.json.JsonObjectBuilder.jsonObjectWith;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FlatJsonPrinterTest {
    private static final Logger log = Logger.getLogger(FlatJsonPrinterTest.class);

    @Test
    public void json_object() {
        FlatJsonPrinter printer = new FlatJsonPrinter();

        Map<String, Object> valueMap = linkedHashMapWith()
                                            .key("foo").value(34)
                                            .key("bar").value("barry")
                                            .build();

        JsonObject jsonObject = new JsonObject(valueMap);

        String result = printer.print(jsonObject).toString();

        log.debug("output:\n" + result);

        assertThat(result, is("{ \"foo\" : 34, \"bar\" : \"barry\" }"));
    }

    @Test
    public void json_array() {
        FlatJsonPrinter printer = new FlatJsonPrinter();

        JsonObject firstObject = jsonObjectWith()
                                    .key("johnny").value("foo")
                                    .key("fooby").value(4435)
                                    .build();

        JsonObject secondObject = jsonObjectWith()
                                    .key("focsle").value("ar!")
                                    .build();


        JsonArray jsonArray = jsonArrayWith()
                                  .items(firstObject, secondObject)
                                  .build();

        String result = printer.print(jsonArray).toString();

        log.debug("output:\n" + result);

        assertThat(result, is("[ { \"johnny\" : \"foo\", \"fooby\" : 4435 }, { \"focsle\" : \"ar!\" } ]"));
    }

}