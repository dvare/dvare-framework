package org.dvare.expression.operation.logical;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.literal.BooleanLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.LogicalOperationExpression;
import org.dvare.expression.operation.OperationType;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.AND)
public class And extends LogicalOperationExpression {
    public And() {
        super(OperationType.AND);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        Boolean left = toBoolean(leftOperand.interpret(instancesBinding));
        Boolean right = toBoolean(rightOperand.interpret(instancesBinding));
        return new BooleanLiteral(left && right);
    }
}