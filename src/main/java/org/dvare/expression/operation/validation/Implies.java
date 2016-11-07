package org.dvare.expression.operation.validation;

import org.dvare.annotations.Operation;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.ValidationOperationExpression;

@Operation(type = OperationType.IMPLIES)
public class Implies extends ValidationOperationExpression {
    public Implies() {
        super(OperationType.IMPLIES);
    }

    public Implies copy() {
        return new Implies();
    }

    @Override
    public Object interpret(Object object) throws InterpretException {


        boolean left = (Boolean) leftOperand.interpret(object);
        boolean right = (Boolean) rightOperand.interpret(object);

        return (!left) | right;
    }

    @Override
    public Object interpret(Object selfRow, Object dataRow) throws InterpretException {
        boolean left = (Boolean) leftOperand.interpret(selfRow, dataRow);
        boolean right = (Boolean) rightOperand.interpret(selfRow, dataRow);
        return (!left) | right;
    }
}