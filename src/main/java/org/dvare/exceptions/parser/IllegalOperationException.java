package org.dvare.exceptions.parser;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class IllegalOperationException extends ExpressionParseException {
    public IllegalOperationException() {
    }

    public IllegalOperationException(String message) {
        super(message);
    }

    public IllegalOperationException(Throwable cause) {
        super(cause);
    }

    public IllegalOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalOperationException(String message, Throwable cause,
                                     boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
