package org.dvare.expression.literal;

import org.apache.commons.lang3.tuple.Triple;
import org.dvare.expression.datatype.TripleType;

public class TripleLiteral extends LiteralExpression<Triple<?, ?, ?>> {

    public TripleLiteral(Triple<?, ?, ?> value) {
        super(value, TripleType.class);
    }

}
