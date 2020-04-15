package org.dvare.builder;


import org.dvare.expression.Expression;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class ExpressionBuilder {

    private Expression expression;

    public ExpressionBuilder() {
    }

    public ExpressionBuilder(Expression expression) {
        this.expression = expression;
    }


    public ExpressionBuilder expression(Expression expression) {
        this.expression = expression;
        return this;
    }

    public Expression build() {
        return expression;
    }

}
