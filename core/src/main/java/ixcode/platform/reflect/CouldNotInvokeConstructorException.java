package ixcode.platform.reflect;

import java.util.List;

import static ixcode.platform.reflect.ParameterSet.printConstructor;
import static ixcode.platform.text.format.CollectionFormat.collectionToString;
import static java.lang.String.format;

public class CouldNotInvokeConstructorException extends RuntimeException {
    public CouldNotInvokeConstructorException(ParameterSet parameterSet,
                                              List<Object> values,
                                              Exception cause) {
        super(message(parameterSet, values, cause));

    }

    private static String message(ParameterSet parameterSet,
                                  List<Object> values,
                                  Exception cause) {

        return format("Failed to invoke constructor (See Cause for more details).\n" +
                              "    Constructor : %s\n" +
                              "    Parameters  : %s",
                      printConstructor(parameterSet),
                      collectionToString(values));

    }


}