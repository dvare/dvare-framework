package org.dvare.expression.literal;


import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.PairType;
import org.dvare.util.Pair;

public class PairLiteral extends LiteralExpression<Pair<?, ?>> {

    public PairLiteral(Pair<?, ?> value) {
        super(value, PairType.class);
    }

    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }
}
