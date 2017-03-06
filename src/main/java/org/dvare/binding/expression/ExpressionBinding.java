package org.dvare.binding.expression;

import org.dvare.expression.Expression;

import java.util.HashMap;


public class ExpressionBinding {
    private HashMap<String, Expression> expressions = new HashMap<>();

    public ExpressionBinding() {

    }

    public ExpressionBinding(HashMap<String, Expression> expressions) {
        if (expressions != null) {
            this.expressions = expressions;
        }
    }

    public void addExpression(String name, Expression expression) {
        if (name != null) {
            expressions.put(name, expression);
        }

    }

    public Expression getExpression(String name) {
        if (expressions.containsKey(name)) {
            return expressions.get(name);
        }
        return null;
    }

    public void removeExpression(String name) {
        if (expressions.containsKey(name)) {
            expressions.remove(name);
        }
    }


    public HashMap<String, Expression> getExpressions() {
        return expressions;
    }

    public void setExpressions(HashMap<String, Expression> expressions) {
        this.expressions = expressions;
    }
}
