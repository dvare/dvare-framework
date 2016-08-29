package org.dvare.expression.literal;


import org.dvare.expression.datatype.DateType;

public class DateLiteral<T> extends LiteralExpression {

    public DateLiteral(T value) {
        super(value, new DateType());
    }
}
