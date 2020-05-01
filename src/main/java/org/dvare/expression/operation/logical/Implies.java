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
@Operation(type = OperationType.IMPLIES)
public class Implies extends LogicalOperationExpression {
    public Implies() {
        super(OperationType.IMPLIES);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        boolean left = toBoolean(leftOperand.interpret(instancesBinding));
        boolean right = toBoolean(rightOperand.interpret(instancesBinding));
        return new BooleanLiteral((!left) | right);
    }
}