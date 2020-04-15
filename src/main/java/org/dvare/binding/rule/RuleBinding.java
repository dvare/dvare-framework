package org.dvare.binding.rule;

import org.dvare.expression.Expression;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class RuleBinding {
    private String name;
    private String rawExpression;
    private Expression expression;

    public RuleBinding(Expression expression) {
        this.expression = expression;
    }


    /* Getter and Setter */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public String getRawExpression() {
        return rawExpression;
    }

    public void setRawExpression(String rawExpression) {
        this.rawExpression = rawExpression;
    }


}
