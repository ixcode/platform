package ixcode.platform.collection;

import org.junit.*;

import java.util.*;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;

public class ApplyTest {

    @Test
    public void can_apply_to_a_collection_and_understand_remaining() {
        List<String> strings = new ArrayList<String>(asList("FOO", "BAR", "JOHNNY"));

        final StringBuilder out = new StringBuilder();

        Apply.the(new Action<String>() {
            @Override public void to(String item, Collection<String> tail) {
                out.append(item);
                String separator =  (tail.size() == 0) ? "" : ", ";
                out.append(separator);
            }
        }).to(strings);

        assertThat(out.toString()).isEqualTo("FOO, BAR, JOHNNY");
    }
}