package org.dvare.expression.literal;

import org.dvare.expression.datatype.NullType;

public class NullLiteral<T> extends LiteralExpression {
    public NullLiteral() {
        super(null, NullType.class);
    }
}
