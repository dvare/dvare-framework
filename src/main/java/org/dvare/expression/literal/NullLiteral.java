package org.dvare.expression.literal;

import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.NullType;

public class NullLiteral<T> extends LiteralExpression<Object> {
    public NullLiteral() {
        super(null, NullType.class);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}
