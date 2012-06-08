package org.xampl;

import org.apache.log4j.Logger;

import java.io.Serializable;

import static org.xampl.AnotherClass.someDeprecatedMethod;


public class SimpleClass implements Serializable {

    private static final Logger log = Logger.getLogger(SimpleClass.class);

    public static void main(String[] args) {
        System.out.println("hello world.");
        someDeprecatedMethod(3);

        iDontCareIfIUseMyOwnDeprecatedMethods();

        String s = (String) "Hello!";

        int divideByZero = 42 / 0;

    }


    /**
     * @deprecated do some other stuff
     */
    public void iShouldProduceAWarning() {

    }

    /**
     * @deprecated Please do something else...
     */
    @Deprecated
    public static void iDontCareIfIUseMyOwnDeprecatedMethods() {
        System.out.println("ooh dont use me");
    }


    class E {
        void m() {
            if (true) ;
        }
    }

    public class ClassWithVarargsMethod {
        void varargsMethod(String... s) { }
    }

    public class ClassWithOverridingMethod extends ClassWithVarargsMethod {
        @Override void varargsMethod(String[] s) { }
    }

}