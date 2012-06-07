import java.io.Serializable;
import java.lang.Deprecated;


public class SimpleClass implements Serializable {

    public static void main(String[] args) {
        System.out.println("hello world.");
        AnotherClass.someDeprecatedMethod(3);

        iDontCareIfIUseMyOwnDeprecatedMethods();

        String s = (String)"Hello!" ;

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
        @Override
        void varargsMethod(String[] s) { }
    }

}