package org.dvare.expression.literal;


import org.dvare.expression.datatype.IntegerType;

public class IntegerLiteral<T> extends LiteralExpression {


    public IntegerLiteral(T value) {
        super(value, new IntegerType());
    }


}
