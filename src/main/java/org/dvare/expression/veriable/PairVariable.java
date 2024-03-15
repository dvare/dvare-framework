package org.dvare.expression.veriable;


import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.PairType;
import org.dvare.util.Pair;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class PairVariable extends VariableExpression<Pair<?, ?>> {


    public PairVariable(String name) {
        this(name, null);
    }

    public PairVariable(String name, Pair<?, ?> value) {
        super(name, PairType.class, value);
    }


    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }
}
