package org.dvare.expression.literal;

import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.TripleType;
import org.dvare.util.Triple;

public class TripleLiteral extends LiteralExpression<Triple<?, ?, ?>> {

    public TripleLiteral(Triple<?, ?, ?> value) {
        super(value, TripleType.class);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}
