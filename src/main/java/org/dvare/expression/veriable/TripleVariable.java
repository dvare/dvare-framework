package org.dvare.expression.veriable;


import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.TripleType;
import org.dvare.util.Triple;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class TripleVariable extends VariableExpression<Triple<?, ?, ?>> {


    public TripleVariable(String name) {
        this(name, null);
    }

    public TripleVariable(String name, Triple<?, ?, ?> value) {
        super(name, TripleType.class, value);
    }


    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }
}
