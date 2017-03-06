package org.dvare.expression.operation.logical;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.operation.LogicalOperationExpression;
import org.dvare.expression.operation.OperationType;

@Operation(type = OperationType.IMPLIES)
public class Implies extends LogicalOperationExpression {
    public Implies() {
        super(OperationType.IMPLIES);
    }


    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {
        boolean left = toBoolean(leftOperand.interpret(expressionBinding, instancesBinding));
        boolean right = toBoolean(rightOperand.interpret(expressionBinding, instancesBinding));
        return (!left) | right;
    }
}