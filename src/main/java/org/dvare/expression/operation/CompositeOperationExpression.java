package org.dvare.expression.operation;


import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;

import java.util.List;

public class CompositeOperationExpression extends Expression {

    private List<Expression> expressions;

    public CompositeOperationExpression(List<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {

        Object result = null;
        for (Expression expression : expressions) {
            result = expression.interpret(expressionBinding, instancesBinding);
        }
        return result;
    }


}
