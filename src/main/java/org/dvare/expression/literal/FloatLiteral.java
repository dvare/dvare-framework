package org.dvare.expression.literal;


import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.FloatType;

public class FloatLiteral extends LiteralExpression<Float> {


    public FloatLiteral(Float value) {
        super(value, FloatType.class);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}
