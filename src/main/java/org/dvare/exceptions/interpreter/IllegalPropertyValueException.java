package org.dvare.exceptions.interpreter;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class IllegalPropertyValueException extends InterpretException {
    public IllegalPropertyValueException() {
    }

    public IllegalPropertyValueException(String message) {
        super(message);
    }

    public IllegalPropertyValueException(Throwable cause) {
        super(cause);
    }

    public IllegalPropertyValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalPropertyValueException(String message, Throwable cause,
                                         boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
