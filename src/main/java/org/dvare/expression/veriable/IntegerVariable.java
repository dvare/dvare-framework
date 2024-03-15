package org.dvare.expression.veriable;

import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.IntegerType;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class IntegerVariable extends VariableExpression<Integer> {

    public IntegerVariable(String name) {
        this(name, null);
    }

    public IntegerVariable(String name, Integer value) {
        super(name, IntegerType.class, value);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}