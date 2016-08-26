package org.dvare.expression.literal;


import org.dvare.expression.datatype.FloatType;

public class FloatLiteral<T> extends LiteralExpression {


    public FloatLiteral(T value) {
        super(value, new FloatType());
    }
}
