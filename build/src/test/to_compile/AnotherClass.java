import sun.reflect.generics.tree.SimpleClassTypeSignature;

import java.util.ArrayList;
import java.util.List;

public class AnotherClass {

    /**
     * @deprecated Please use some other method now
     */
    @Deprecated
    public static void someDeprecatedMethod(int x) {
        List uncheckedList = new ArrayList();

        uncheckedList.add("foo");

        try {
            System.out.println("foo");

            switch (x) {
                case 1:
                    System.out.println("1");
                    //  No  break;  statement here.
                case 2:
                    System.out.println("2");
            }
        } finally {
            throw new RuntimeException();
        }



    }
}