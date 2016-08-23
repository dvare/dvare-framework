package com.dvare.expression.literal;


import com.dvare.expression.datatype.BooleanType;

public class BooleanLiteral<T> extends LiteralExpression {

    public BooleanLiteral(T value) {
        super(value, new BooleanType());
    }
}
