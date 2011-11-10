package ixcode.platform.text;

import org.junit.Test;

import static ixcode.platform.text.CamelCaseTransform.splitCamelCase;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CamelCaseTransformTest {

    @Test
    public void splits_camel_case_with_a_hyphen() {
        String input = "someCamelCasedString";

        String output = splitCamelCase(input, "-");

        assertThat(output, is("some-camel-cased-string"));
    }

}