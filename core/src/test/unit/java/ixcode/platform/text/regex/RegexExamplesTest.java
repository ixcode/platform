package ixcode.platform.text.regex;

import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RegexExamplesTest {

    private static final Logger log = Logger.getLogger(RegexExamplesTest.class);

    @Test
    public void find_multiple_groups() {
        Pattern pattern = compile("\\{\\w*\\}");
        Matcher matcher = pattern.matcher("/along/{parameter1}/some/path/{parameter2}/end");

        List<String> parameterNames = new ArrayList<String>();
        while (matcher.find()) {
            String parameterName = matcher.group().replaceAll("[\\{\\}]", "");
            log.info("Found parameter: [" + parameterName + "]");
            parameterNames.add(parameterName);
        }

        assertThat(parameterNames.size(), is(2));
        assertThat(parameterNames.get(0), is("parameter1"));
        assertThat(parameterNames.get(1), is("parameter2"));

        String output = matcher.replaceAll("(.*)");
        log.info("Replaced output: [" + output + "]");

        assertThat(output, is("/along/(.*)/some/path/(.*)/end"));

    }

    @Test
    public void extract_parameters() {
        Pattern pattern = compile("/along/(.*)/some/path/(.*)/end");

        Matcher matcher = pattern.matcher("/along/VALUE1/some/path/VALUE2/end");

        log.debug("\n" +  new MatcherPrinter(matcher).toString());


        assertThat(matcher.group(1), is("VALUE1"));
        assertThat(matcher.group(2), is("VALUE2"));
    }




}