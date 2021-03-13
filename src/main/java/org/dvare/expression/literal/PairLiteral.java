package org.dvare.expression.literal;


import org.apache.commons.lang3.tuple.Pair;
import org.dvare.expression.datatype.PairType;

public class PairLiteral extends LiteralExpression<Pair<?, ?>> {

    public PairLiteral(Pair<?, ?> value) {
        super(value, PairType.class);
    }

}
