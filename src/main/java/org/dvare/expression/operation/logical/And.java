package org.dvare.expression.operation.logical;

import org.dvare.annotations.Operation;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.operation.LogicalOperationExpression;
import org.dvare.expression.operation.OperationType;

@Operation(type = OperationType.AND)
public class And extends LogicalOperationExpression {
    public And() {
        super(OperationType.AND);
    }

    public And copy() {
        return new And();
    }


    @Override
    public Object interpret(Object object) throws InterpretException {
        Boolean left = toBoolean(leftOperand.interpret(object));
        Boolean right = toBoolean(rightOperand.interpret(object));

        return left && right;
    }


    @Override
    public Object interpret(Object selfRow, Object dataRow) throws InterpretException {
        Boolean left = toBoolean(leftOperand.interpret(selfRow, dataRow));
        Boolean right = toBoolean(rightOperand.interpret(selfRow, dataRow));
        return left && right;
    }
}