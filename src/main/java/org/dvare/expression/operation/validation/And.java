package org.dvare.expression.operation.validation;

import org.dvare.annotations.Operation;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.ValidationOperationExpression;

@Operation(type = OperationType.AND)
public class And extends ValidationOperationExpression {
    public And() {
        super(OperationType.AND);
    }

    public And copy() {
        return new And();
    }


    @Override
    public Object interpret(Object object) throws InterpretException {
        Boolean left = (Boolean) leftOperand.interpret(object);
        Boolean right = (Boolean) rightOperand.interpret(object);

        return left && right;
    }


    @Override
    public Object interpret(Object selfRow, Object dataRow) throws InterpretException {
        boolean left = (Boolean) leftOperand.interpret(selfRow, dataRow);
        boolean right = (Boolean) rightOperand.interpret(selfRow, dataRow);
        return left && right;
    }
}