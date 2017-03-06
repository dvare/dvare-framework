package org.dvare.expression.operation.logical;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.operation.LogicalOperationExpression;
import org.dvare.expression.operation.OperationType;

@Operation(type = OperationType.AND)
public class And extends LogicalOperationExpression {
    public And() {
        super(OperationType.AND);
    }


    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {
        Boolean left = toBoolean(leftOperand.interpret(expressionBinding, instancesBinding));
        Boolean right = toBoolean(rightOperand.interpret(expressionBinding, instancesBinding));
        return left && right;
    }
}