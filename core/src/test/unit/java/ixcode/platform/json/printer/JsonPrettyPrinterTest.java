package ixcode.platform.json.printer;

import org.junit.Test;

import static ixcode.platform.json.JsonObjectBuilder.jsonObjectWith;
import static ixcode.platform.json.printer.JsonPrettyPrinter.prettyPrintJson;
import static java.util.Arrays.asList;

public class JsonPrettyPrinterTest {

    @Test
    public void nicely_prints_some_json() {

        String input = jsonObjectWith()
                .attribute("is", "someobject")
                .attribute("name", "johnny")
                .attribute("color", "pink")
                .serialize();

        String output = prettyPrintJson(input);

        System.out.println(output);
    }

    @Test
    public void deals_with_arrays_aswell() {
        String input = jsonObjectWith()
                .attribute("is", "someobject")
                .attribute("name", "johnny")
                .attribute("colors", asList("pink", "green", "blue", "orange"))
                .serialize();

        String output = prettyPrintJson(input);

        System.out.println(output);

    }
}