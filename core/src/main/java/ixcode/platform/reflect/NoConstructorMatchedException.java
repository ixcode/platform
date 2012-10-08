package ixcode.platform.reflect;

import ixcode.platform.collection.Action;
import ixcode.platform.collection.FArrayList;

import java.util.Collection;
import java.util.Set;

import static ixcode.platform.reflect.ParameterSet.printConstructor;
import static ixcode.platform.text.format.CollectionFormat.collectionToString;
import static java.lang.String.format;

public class NoConstructorMatchedException extends RuntimeException {


    public NoConstructorMatchedException(Class<?> targetClass,
                                         Set<String> parameterNames,
                                         Set<ParameterSet> parameterSets) {
        super(message(targetClass, parameterNames, parameterSets));
    }

    private static String message(Class<?> targetClass,
                                  Set<String> parameterNames,
                                  Set<ParameterSet> parameterSets) {

        return format("\n    Target Class: [%s]\n    Input parameter names: [%s]\n"
                              + "    Found these constructors: [\n %s\n    ]",
                      targetClass.getName(),
                      collectionToString(parameterNames),
                      printConstructors(targetClass, parameterSets));
    }

    private static String printConstructors(final Class<?> targetClass, Set<ParameterSet> parameterSets) {
        final StringBuilder sb = new StringBuilder();

        new FArrayList<ParameterSet>(parameterSets).apply(new Action<ParameterSet>() {
            @Override public void to(ParameterSet item, Collection<ParameterSet> tail) {
                sb.append("      ")
                  .append(printConstructor(item));

                if (!tail.isEmpty()) {
                    sb.append(",\n");
                }
            }
        });


        return sb.toString();
    }


}