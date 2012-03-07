package ixcode.platform.serialise;

import ixcode.platform.text.format.ObjectFormatter;
import ixcode.platform.text.format.UriFormat;
import ixcode.platform.xml.XmlStringBuilder;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static ixcode.platform.reflect.TypeChecks.isList;
import static ixcode.platform.reflect.TypeChecks.isMap;
import static java.lang.String.format;

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

    private <T> void appendObject(T object) {
        if (isList(object)) {
            appendList(object);
        } else if (isMap(object.getClass())) {
            appendMap(object);
        } else if (URI.class.isAssignableFrom(object.getClass())) {
            xb.appendText(new UriFormat().format(object));
        } else {
            appendActualObject(object);
        }
    }

    private <T> void appendMap(T object) {
        Map<?, ?> theMap = (Map<?, ?>) object;

        for (Map.Entry<?, ?> entry : theMap.entrySet()) {
            xb.openContainerNode(entry.getKey().toString());

            appendObject(entry.getValue());

            xb.closeContainerNode(entry.getKey().toString());
        }
    }

    private <T> void appendList(T object) {
        List<?> theList = (List<?>) object;

        for (Object item : theList) {
            appendObject(item);
        }

    }


    private <T> void appendActualObject(T objectToSerialise) {
        String nodeName = formatNodeName(objectToSerialise);

        xb.openContainerNode(nodeName);
        appendObjectGuts(objectToSerialise);
        xb.closeContainerNode(nodeName);
        xb.newline();
    }

    protected <T> String formatNodeName(T objectToSerialise) {
        return prettyName(objectToSerialise.getClass().getSimpleName());
    }

    private static String prettyName(String simpleName) {
        return format("%s%s", simpleName.substring(0, 1).toLowerCase(), simpleName.substring(1));
    }

    protected <T> void appendObjectGuts(T object) {
        if (String.class.isAssignableFrom(object.getClass())) {
            xb.appendText(object.toString());
            return;
        }

        reflectivelyAppend(object);
    }

    private <T> void reflectivelyAppend(T simpleObject) {
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