package ixcode.platform.xml;

import java.lang.reflect.*;

import static java.lang.String.format;

public class XmlSerialiser {
    private final XmlStringBuilder xb = new XmlStringBuilder();

    public String toXml(Object objectToSerialise) {
        appendObject(objectToSerialise);
        return xb.toString();
    }

    private void appendObject(Object objectToSerialise) {
        String nodeName = prettyName(objectToSerialise.getClass().getSimpleName());

        xb.openContainerNode(nodeName);
        xb.newline();

        appendFields(objectToSerialise);

        xb.closeContainerNode(nodeName);
    }

    private static String prettyName(String simpleName) {
        return format("%s%s", simpleName.substring(0, 1).toLowerCase(), simpleName.substring(1));
    }

    private void appendFields(Object simpleObject) {
        Field[] declaredFields = simpleObject.getClass().getDeclaredFields();

        for (Field f : declaredFields) {
            xb.openValueNode(f.getName());

            try {
                xb.appendText(f.get(simpleObject).toString().trim());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            xb.closeValueNode(f.getName());
            xb.newline();
        }
    }
}