package org.dvare.expression.veriable;


import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.FloatType;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class FloatVariable extends VariableExpression<Float> {


    public FloatVariable(String name) {
        this(name, null);
    }


    public FloatVariable(String name, Float value) {
        super(name, FloatType.class, value);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}