package org.dvare.exceptions.interpreter;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class FunctionCallException extends InterpretException {
    public FunctionCallException() {
    }

    public FunctionCallException(String message) {
        super(message);
    }

    public FunctionCallException(Throwable cause) {
        super(cause);
    }

    public FunctionCallException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunctionCallException(String message, Throwable cause,
                                 boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
