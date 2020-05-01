package org.dvare.expression.operation;

import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.LiteralExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class CompositeOperationExpression extends Expression {

    private List<Expression> expressions = new ArrayList<>();

    public CompositeOperationExpression(List<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {

        LiteralExpression<?> result = null;
        for (Expression expression : expressions) {
            result = expression.interpret(instancesBinding);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        if (expressions != null) {
            for (Expression expression : expressions) {
                toStringBuilder.append(expression);
                toStringBuilder.append(" ");
            }
        }

        return toStringBuilder.toString();
    }
}
