package ixcode.platform.exception;

import static java.lang.String.format;

public class RuntimeExceptionX extends java.lang.RuntimeException {


    public RuntimeExceptionX(Throwable cause) {
        super("(See Cause).", cause);
    }

    public RuntimeExceptionX(String message, Throwable cause) {
        super(format("%s. (See Cause).", message).trim(), cause);
    }


}