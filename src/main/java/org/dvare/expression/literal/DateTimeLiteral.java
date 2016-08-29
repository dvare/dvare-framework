package org.dvare.expression.literal;


import org.dvare.expression.datatype.DateTimeType;

public class DateTimeLiteral<T> extends LiteralExpression {


    public DateTimeLiteral(T value) {
        super(value, new DateTimeType());
    }
}
