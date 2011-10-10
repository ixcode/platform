package ixcode.platform.serialise;

import ixcode.platform.json.JsonObject;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.Map;

import static ixcode.platform.collection.MapBuilder.linkedHashMapWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FlatJsonPrinterTest {
    private static final Logger log = Logger.getLogger(FlatJsonPrinterTest.class);

    @Test
    public void prints_some_json() {
        FlatJsonPrinter printer = new FlatJsonPrinter();

        Map<String, Object> valueMap = linkedHashMapWith()
                                            .key("foo").value(34)
                                            .key("bar").value("barry")
                                            .build();

        String result = printer.print(new JsonObject(valueMap)).toString();

        log.debug("output:\n" + result);

        assertThat(result, is("{ \"foo\" : 34, \"bar\" : \"barry\" }"));
    }

}