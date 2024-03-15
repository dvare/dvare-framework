package org.dvare.expression.literal;


import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.IntegerType;

public class IntegerLiteral extends LiteralExpression<Integer> {


    public IntegerLiteral(Integer value) {
        super(value, IntegerType.class);
    }


    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}
