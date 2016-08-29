package org.dvare.expression.literal;


import org.dvare.expression.datatype.BooleanType;

public class BooleanLiteral<T> extends LiteralExpression {

    public BooleanLiteral(T value) {
        super(value, new BooleanType());
    }
}
