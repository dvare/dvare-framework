package org.dvare.expression.literal;


import org.dvare.expression.datatype.FloatType;

public class FloatLiteral extends LiteralExpression<Float> {


    public FloatLiteral(Float value) {
        super(value, FloatType.class);
    }
}
