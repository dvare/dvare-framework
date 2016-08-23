package com.dvare.expression.literal;


import com.dvare.expression.datatype.DateTimeType;

public class DateTimeLiteral<T> extends LiteralExpression {


    public DateTimeLiteral(T value) {
        super(value, new DateTimeType());
    }
}
