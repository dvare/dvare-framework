package org.dvare.exceptions.parser;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class ExpressionParseException extends Exception {
    public ExpressionParseException() {
    }

    public ExpressionParseException(String message) {
        super(message);
    }

    public ExpressionParseException(Throwable cause) {
        super(cause);
    }

    public ExpressionParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpressionParseException(String message, Throwable cause,
                                    boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
