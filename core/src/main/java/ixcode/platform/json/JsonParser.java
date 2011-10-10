package ixcode.platform.json;

import net.nextquestion.json.*;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

import java.util.*;

public class JsonParser {
    public <T> T parse(String jsonString) {
        return parseFromLexer(createStringLexer(jsonString));
    }

    private static <T> T parseFromLexer(JSONLexer jsonLexer) {
        JSONTree jsonTree = parseTree(jsonLexer);
        try {
            Object value = jsonTree.value();
            return parseObject(value);
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
    }

    private static JSONTree parseTree(JSONLexer lexer) {
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JSONParser parser = new JSONParser(tokens);

        try {
            JSONParser.value_return r = parser.value();
            return createTreeFrom(tokens, r);
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }

    }

    private static Map<String, Object> parseMap(Map<Object, Object> valueMap) {
        Map<String, Object> resultingMap = new LinkedHashMap<String, Object>();
        for (Map.Entry<Object, Object> entry : valueMap.entrySet()) {
            resultingMap.put((String) entry.getKey(), parseObject(entry.getValue()));
        }
        return resultingMap;
    }

    private static <T> T parseObject(Object value) {
        if (value == null) {
            return null;
        }

        Class<?> valueClass = value.getClass();

        if (Map.class.isAssignableFrom(valueClass)) {
            return (T) jsonObjectFrom((Map) value);
        } else if (List.class.isAssignableFrom(valueClass)) {
            return (T) jsonArrayFrom((List) value);
        } else if (String.class.isAssignableFrom(valueClass)) {
            return (T) value;
        } else if (Integer.class.isAssignableFrom(valueClass)
                || Double.class.isAssignableFrom(valueClass)) {
            return (T) value;
        } else if (Boolean.class.isAssignableFrom(valueClass)) {
            return (T) (Boolean)value;
        }

        throw new RuntimeException("Could not parse a JsonObject or a JsonArray from valueClass " + valueClass.getName());
    }



    private static JSONTree createTreeFrom(CommonTokenStream tokens, JSONParser.value_return r) {
        CommonTree t = (CommonTree) r.getTree();
        CommonTreeNodeStream nodes = new CommonTreeNodeStream(t);
        nodes.setTokenStream(tokens);

        return new JSONTree(nodes);
    }

    private static JSONLexer createStringLexer(String text) {
        return new JSONLexer(new ANTLRStringStream(text));
    }

    private static JsonArray jsonArrayFrom(List values) {
        List<Object> jsonValues = new ArrayList<Object>();
        for (Object value : values) {
            jsonValues.add(parseObject(value));
        }
        return new JsonArray(jsonValues);
    }

    private static JsonObject jsonObjectFrom(Map valueMap) {
        return new JsonObject(parseMap(valueMap));
    }


}