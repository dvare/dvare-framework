package org.dvare.exceptions.parser;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class IllegalLiteralException extends IllegalValueException {

    private String literalName;

    public IllegalLiteralException(String literalName, String message) {
        super(message);
        this.literalName = literalName;
    }

    public IllegalLiteralException(String literalName, String message, Throwable e) {
        super(message, e);
        this.literalName = literalName;
    }


    public String getLiteralName() {
        return literalName;
    }
}
