package ixcode.platform.serialise;

import ixcode.platform.collection.*;
import ixcode.platform.json.*;
import ixcode.platform.reflect.*;
import ixcode.platform.text.*;

import java.lang.reflect.*;
import java.util.*;

import static ixcode.platform.reflect.ObjectReflector.reflect;
import static ixcode.platform.reflect.TypeChecks.*;

public class JsonSerialiser {

    private final JsonBuilder builder = new JsonBuilder();
    private final JsonPrinter printer = new FlatJsonPrinter();

    public <T> String toJson(T object) {
        return printer.print(builder.buildFrom(object));
    }

}