package org.dvare.exceptions.parser;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class IllegalValueException extends ExpressionParseException {
    public IllegalValueException() {
    }

    public IllegalValueException(String message) {
        super(message);
    }

    public IllegalValueException(Throwable cause) {
        super(cause);
    }

    public IllegalValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalValueException(String message, Throwable cause,
                                 boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
