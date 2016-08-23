package com.dvare.expression.literal;


import com.dvare.expression.datatype.DateType;

public class DateLiteral<T> extends LiteralExpression {

    public DateLiteral(T value) {
        super(value, new DateType());
    }
}
