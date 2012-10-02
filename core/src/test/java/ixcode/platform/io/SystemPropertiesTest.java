package ixcode.platform.io;

import org.junit.Test;

import java.util.Properties;

import static java.lang.String.format;
import static java.lang.System.getProperties;
import static java.lang.System.out;

public class SystemPropertiesTest {

    @Test
    public void list_all_system_properties() {

        Properties properties = getProperties();

        for (Object key : properties.keySet()) {
            out.println(format("%s=%s", key, properties.getProperty((String)key)));
        }

    }


}