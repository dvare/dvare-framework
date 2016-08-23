package com.dvare.expression.literal;


import com.dvare.expression.datatype.IntegerType;

public class IntegerLiteral<T> extends LiteralExpression {


    public IntegerLiteral(T value) {
        super(value, new IntegerType());
    }


}
