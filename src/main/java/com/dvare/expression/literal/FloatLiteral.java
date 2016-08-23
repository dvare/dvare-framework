package com.dvare.expression.literal;


import com.dvare.expression.datatype.FloatType;

public class FloatLiteral<T> extends LiteralExpression {


    public FloatLiteral(T value) {
        super(value, new FloatType());
    }
}
