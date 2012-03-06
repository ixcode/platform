package ixcode.platform.serialise;

import ixcode.platform.text.format.ObjectFormatter;
import ixcode.platform.xml.XmlStringBuilder;

import java.lang.reflect.Field;
import java.util.List;

import static ixcode.platform.reflect.TypeChecks.isList;
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
        if (isList(object)) {
            appendList(object);
        } else {
            appendObject(object);
        }

        return xb.toString();
    }

    private <T> void appendList(T object) {
        List<?> theList = (List<?>)object;

        for (Object item : theList) {
            appendObject(item);
        }

    }


    private <T> void appendObject(T objectToSerialise) {
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
        
        appendSimpleObject(object);
    }

    private <T> void appendSimpleObject(T simpleObject) {Field[] declaredFields = simpleObject.getClass().getDeclaredFields();

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