package org.dvare.expression.veriable;


import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.RegexType;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class RegexVariable extends VariableExpression<String> {

    public RegexVariable(String name) {
        this(name, null);
    }

    public RegexVariable(String name, String value) {
        super(name, RegexType.class, value);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}