package org.dvare.expression.literal;


import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.BooleanType;

public class BooleanLiteral extends LiteralExpression<Boolean> {

    public BooleanLiteral(Boolean value) {
        super(value, BooleanType.class);
    }

    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }
}
