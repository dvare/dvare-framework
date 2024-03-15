package org.dvare.expression.veriable;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */

import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.BooleanType;

public class BooleanVariable extends VariableExpression<Boolean> {

    public BooleanVariable(String name) {
        this(name, null);

    }

    public BooleanVariable(String name, Boolean value) {
        super(name, BooleanType.class, value);

    }


    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }
}
