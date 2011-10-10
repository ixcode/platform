package ixcode.platform.serialise;

import ixcode.platform.text.*;
import ixcode.platform.xml.*;

import java.lang.reflect.*;

import static java.lang.String.*;

public class XmlSerialiser {
    protected final XmlStringBuilder xb;
    private final ObjectFormatter formatter;

    public XmlSerialiser() {
        this(0);
    }

    public XmlSerialiser(int currentIndent) {
        xb = new XmlStringBuilder(currentIndent);
        formatter = new ObjectFormatter();
    }


    public <T> String toXml(T object) {
        appendObject(object);
        return xb.toString();
    }

    private <T> void appendObject(T objectToSerialise) {
        String nodeName = formatNodeName(objectToSerialise);

        xb.openContainerNode(nodeName);
        xb.newline();

        appendObjectGuts(objectToSerialise);

        xb.closeContainerNode(nodeName);
    }

    protected <T> String formatNodeName(T objectToSerialise) {
        return prettyName(objectToSerialise.getClass().getSimpleName());
    }

    private static String prettyName(String simpleName) {
        return format("%s%s", simpleName.substring(0, 1).toLowerCase(), simpleName.substring(1));
    }

    protected <T> void appendObjectGuts(T simpleObject) {
        Field[] declaredFields = simpleObject.getClass().getDeclaredFields();

        for (Field f : declaredFields) {
            xb.node(f.getName());

            try {
                Object objValue = f.get(simpleObject);
                xb.appendText(formatter.format(objValue).trim());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            xb.closeNode(f.getName());
            xb.newline();
        }
    }


}